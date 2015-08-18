var stompClient = null;

Highcharts.setOptions({
    global: {
        useUTC: false
    }
});

function connect() {
    console.log("Connecting to websocket");
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/state', function(frame){
            handleStateUpdate(JSON.parse(frame.body));
        });
    });
}

function handleStateUpdate(state) {
    console.log("Received a state update: " + state);

    var itemId = state.itemId;
    var stateItems = state.stateItems;
    $.each(stateItems, function (i, stateItem) {
        var label = stateItem.label;
        if(label == "on-off") {
            console.log("Received an on-off event: " + stateItem.value.value);

            var iSwitch = $("input[name=" + itemId + "_switch]");

            var value = stateItem.value.value;
            if(value == "off") {
                //set switch to off and not trigger event
                iSwitch.bootstrapSwitch("state", false, true);
            } else {
                //set switch to on and not trigger event
                iSwitch.bootstrapSwitch("state", true, true);
            }
        } else if(label == "value") {
            //slider value potentially
            console.log("Setting slider value: " + stateItem.value.value + " for device: " + itemId)
            var iDimmer = $("input[name=" + itemId + "_slider]");
            iDimmer.slider('setValue', stateItem.value.value);
        } else if(label == "rgb") {
            console.log("Setting rgb: " + stateItem.value.value + " for device: " + itemId);

            var colorPicker = $("input[name=" + itemId + "_color]");
            colorPicker.spectrum("set", stateItem.value.value);

        } else {
            //most likely a raw value on a label
            console.log("Checking for label for item: " + itemId + " with label: " + label)
            var valueLabel = $("label[itemId=" + itemId + "][labelId=" + label + "]");

            var rawValue = stateItem.value.value;
            if (valueLabel) {
                valueLabel.text(rawValue);
            }

            var graphs = $("li.graph[itemId=" + itemId + "][labelId=" + label + "]");
            if(graphs) {
                $.each(graphs, function(i, graph) {
                    var widgetId = graph.getAttribute("id");
                    console.log("Updating graph with id: " + widgetId);

                    var widget = $("#" + widgetId);
                    var series = widget.highcharts().series;
                    var time = (new Date).getTime();

                    console.log("Adding datapoint: " + time + " val: " + rawValue);
                    series[0].addPoint([time, rawValue]);
                });
            }

        }
    })
}

function tabChange(e) {
    e.preventDefault();

    var currentDashboard = $("#dashboards").attr("dashboardId");
    $("#dashlink_" + currentDashboard).removeClass('active');

    var targetDashboardId = this.getAttribute('dashboardId');
    $("#dashlink_" + targetDashboardId).addClass('active');

    console.log("Tab change target dash: " + targetDashboardId);

    renderDashboard(targetDashboardId);
}

function tabAdd(e) {
    e.preventDefault();

    $("#createContainerForm").attr("mode", "dashboard");
    $("#addContainerLabel").text("Add a dashboard");

    $("#containerModal").modal('show');

    console.log("Adding tab");
}

function renderDashboardsLinks() {
    $.get("/dashboards/", function(data) {
        var dashboardTabs = $('#dashboards');
        dashboardTabs.empty();

        $.each(data, function(i, item) {
            var tabClass = "notActive";
            if(i == 0) {
                tabClass = "active";
            }

            var data = {"dashboardId": item.id, "dashboardName": item.name, "weight": item.weight, "tabClass" : tabClass};
            dashboardTabs.append(renderTemplate("tabTemplate", data));
        });

        dashboardTabs.append("<li role=\"presentation\"><a class=\"addTab\" href=\"#add\"><span class=\"glyphicon glyphicon-plus\"></span></a></li>");

        $(".tab").click(tabChange);
        $(".addTab").click(tabAdd);
    });
}

function renderDefaultDashboard() {
    $.get("/dashboards/default", function(data) {
        console.log("Default dashboard: " + data.id);

        renderDashboardWithName(data.name, data.id);
    });
}

function renderDashboard(dashboardId) {
    console.log("Rendering dashboard: " + dashboardId);
    $.get("/dashboards/(" + dashboardId + ")", function(data) {
        renderDashboardWithName(data.name, data.id);
    });
}

function renderDashboardWithName(name, dashboardId) {
    $("#dashboardTitle").text("Dashboard: " + name);
    $("#dashboards").attr("dashboardId", dashboardId);
    $("#container").empty();

    $.get("/ui/dashboard(" + dashboardId + ")/containers", function(data) {
        $.each(data, function(i, item) {
            renderContainer(item);
        })
    });
}

function renderContainerById(containerId) {
    $.get("/ui/containers(" + containerId + ")", function(data) {
        renderContainer(data);
    })

}

function renderContainer(item) {
    var containerId = item.id;
    var name = item.name;

    console.log("Rendering container: " + containerId + " name: " + name);

    var data = {
        "containerId" : containerId,
        "name" : name
    };

    var rendered = renderTemplate("containerTemplate", data);
    $("#container").append(rendered);
    var container = $("ul[containerId=" + containerId + "]");
    container.disableSelection();
    container.sortable({
        revert: true,
        connectWith: ".sortable",
        update: function( event, ui ) {
            console.log("Parent: " + $(this).attr("containerId"));

            $(this).children("li").each(function(index) {
                var widgetId = $(this).attr("id");
                console.log("Widget: " + widgetId + " position: " + index);

                $.ajax({url: "/ui/items(" + widgetId + ")/setWeight(" + index + ")", type: "POST", data: {}, dataType: "json", contentType: "application/json; charset=utf-8"});

            });
        },
        receive: function( event, ui ) {
            var containerId = $(this).attr("containerId");
            var widgetId = ui.item.attr("id");

            console.log("Setting Parent: " + containerId + " for widget: " + widgetId);

            $.ajax({url: "/ui/items(" + widgetId + ")/setParent(" + containerId + ")", type: "POST", data: {}, dataType: "json", contentType: "application/json; charset=utf-8"});
        }
    });

    renderContainerItems(containerId);
}

function renderContainerItems(containerId) {
    $.get("/ui/containers(" + containerId + ")/items", function (data) {
        $.each(data, function (i, item) {
            renderWidget(containerId, item);
        })
    })
}

function renderWidget(containerId, item) {
    console.log("Rendering widget: " + item.id + " in container: " + containerId);
    var widgetType = item.uiType;
    switch (widgetType.toLowerCase()) {
        case "switch":
            renderSwitch(item, containerId);
            break;
        case "dimmer":
            renderSlider(item, containerId);
            break;
        case "label":
            renderLabel(item, containerId);
            break;
        case "graph":
            renderGraph(containerId, item);
            break;
        case "color":
            renderColorPicker(containerId, item);
            break;
        default:
            console.log("Unsupported widget type: " + widgetType + " for item: " + item.name);
    }
}

function renderColorPicker(containerId, item) {
    console.log("Rendering color picker widget for item: " + item.name);

    var data = {
        "widgetId": item.id,
        "name": item.name,
        "itemId": item.itemId,
        "weight" : item.weight
    }

    renderWidgetTemplate("colorTemplate", data, item.id, containerId);

    var cPicker = $("input[name=" + item.itemId + "_color]");

    cPicker.spectrum({
        change: function(color) {
            handleColorChange(cPicker, color);
        },
        showButtons: false
    });
}

function handleColorChange(picker, color) {
    console.log("Target color: " + color.toHexString());

    var data = {
        "itemId" : picker.attr('itemId'),
        "commandType" : "value",
        "properties" : {
            "rgb" : color.toHexString()
        }
    }
    var jsonData = JSON.stringify(data);
    console.log("Sending command: " + jsonData);

    $.ajax({url: "/command/send", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
        console.log("Posted Command successfully");
    }})

}

function renderGraph(containerId, item) {
    console.log("Rendering graph for item: " + item.name);

    var label = item.properties.label;
    var unit = item.properties.unit;

    var data = {
        "widgetId": item.id,
        "itemId": item.itemId,
        "name": item.name,
        "label": label,
        "weight" : item.weight
    }

    renderWidgetTemplate("graphTemplate", data, item.id, containerId);

    var widget = $("#" + item.id);
    widget.highcharts({
        chart: {
            type: 'area'
        },
        title: {
            text: item.name
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: unit
            }
        },
        plotOptions: {
            area: {
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
                marker: {
                    enabled: false,
                    symbol: 'circle',
                    radius: 2,
                    states: {
                        hover: {
                            enabled: true
                        }
                    }
                }
            }
        },
        series: [{
            name: unit,
            data : []
        }]
    });

    $.get("/timeseries/item(" + item.itemId + ")/label(" + label + ")", function(data) {
        var array = [];
        $.each(data, function(i, point) {
            var value = point.value;
            var timestamp = point.timestamp;

            array.push([timestamp, value]);
        });
        var series = widget.highcharts().series;
        series[0].setData(array);
    });


}

function renderSlider(item, containerId) {
    console.log("Rendering dimmer for item: " + item.name)

    var data = {
        "widgetId": item.id,
        "name": item.name,
        "itemId": item.itemId,
        "weight" : item.weight
    }

    renderWidgetTemplate("sliderTemplate", data, item.id, containerId);

    var iDimmer = $("input[name=" + item.itemId + "_slider]");
    iDimmer.slider();
    iDimmer.on("slideStop", handleSlideEvent);
}

function handleSlideEvent(slideEvt) {
    var val = slideEvt.value;
    console.log("Slide event: " + val);

    var data = {
        "itemId" : this.getAttribute('itemId'),
        "commandType" : "value",
        "properties" : {
            "value" : val
        }
    }
    var jsonData = JSON.stringify(data);
    console.log("Sending command: " + jsonData);

    $.ajax({url: "/command/send", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
        console.log("Posted Command successfully");
    }})

}

function renderSwitch(item, containerId) {
    console.log("Rendering switch for item: " + item.name)

    var data = {
        "widgetId": item.id,
        "name": item.name,
        "itemId": item.itemId,
        "label": "on-off",
        "weight" : item.weight
    }

    renderWidgetTemplate("switchTemplate", data, item.id, containerId);

    var iSwitch = $("input[name=" + item.itemId + "_switch]");
    iSwitch.bootstrapSwitch();
    iSwitch.on('switchChange.bootstrapSwitch', handleSwitchEvent);

    //lets get the initial state for the widget
    forceUpdateDeviceState(item.itemId);
}

function handleSwitchEvent(event, state) {
    var command = "off";
    if(state) {
        command = "on";
    }
    var data = {
        "itemId" : this.getAttribute('itemId'),
        "commandType" : "switch",
        "properties" : {
            "value" : command
        }
    }
    var jsonData = JSON.stringify(data);
    console.log("Sending command: " + jsonData);

    $.ajax({url: "/command/send", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
        console.log("Posted Command successfully");
    }})
}

function renderLabel(item, containerId) {
    console.log("Rendering label widget for item: " + item.name)

    var label = item.properties.label;
    var unit = item.properties.unit;

    var data = {
        "widgetId": item.id,
        "name": item.name,
        "value": 0,
        "itemId": item.itemId,
        "label": label,
        "unit": unit,
        "weight" : item.weight
    }

    renderWidgetTemplate("labelTemplate", data, item.id, containerId)

    //lets get the initial state for the widget
    forceUpdateDeviceState(item.itemId);
}

function renderWidgetTemplate(templateName, data, itemId, containerId) {
    var rendered = renderTemplate(templateName, data);
    appendContainer(rendered, itemId, containerId)
}

function renderTemplate(templateName, data) {
    var template = $('#' + templateName).html();
    Mustache.parse(template);
    return Mustache.render(template, data);
}

function forceUpdateDeviceState(itemId) {
    $.get("/data/state(" + itemId + ")", function(data){
        if(!isEmpty(data)) {
            handleStateUpdate(data);
        }
    });
}

function appendContainer(widgetHtml, elementId, containerId) {
    if ($("#" + elementId).length > 0) {
        //widget already exists
    } else {
        var container = $("ul[containerId=" + containerId + "]");
        container.append(widgetHtml);
    }
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}


$(document).ready(function() {
    renderDashboardsLinks();
    renderDefaultDashboard();

    connect();
});