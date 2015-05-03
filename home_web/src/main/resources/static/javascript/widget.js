$(document).ready(function() {
    var stompClient = null;

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
            } else if(label == "temperature") {
                console.log("Checking for label for item: " + itemId + " with label: " + label)
                var valueLabel = $("label[deviceId=" + itemId + "][labelId=" + label + "]");

                if (valueLabel) {
                    valueLabel.text(stateItem.value.value);
                }
            }
        })
    }

    $('div[containerId]').each(function(i,el) {

        var containerId = el.getAttribute("containerId");
        console.log(containerId);

        $.get("/ui/containers/" + containerId + "/items", function(data) {
            $.each(data, function(i, item) {
                renderWidget(containerId, item);
            })

            connect();
        })
    });

    function renderWidget(containerId, item) {
        console.log("Rendering widget: " + item.id);
        var widgetType = item.uiType;
        switch (widgetType.toLowerCase()) {
            case "switch":
                renderSwitch(item, containerId);
                break;
            case "dimmer":
                break;
            case "temperature":
                renderTemperature(item, containerId);
                break;
            default:
                console.log("Unsupported widget type: " + widgetType + " for item: " + item.name);
        }
    }

    function renderSwitch(item, containerId) {
        console.log("Rendering switch for item: " + item.name)

        var data = {
            "widgetId": item.id,
            "name": item.name,
            "deviceId": item.deviceId,
            "label": "on-off"
        }

        renderWidgetTemplate("switchTemplate", data, item.id, containerId);

        var iSwitch = $("input[name=" + item.deviceId + "_switch]");
        iSwitch.bootstrapSwitch();
        iSwitch.on('switchChange.bootstrapSwitch', handleSwitchEvent);

        //lets get the initial state for the widget
        forceUpdateDeviceState(item.deviceId);
    }

    function handleSwitchEvent(event, state) {
        var command = "off";
        if(state) {
            command = "on";
        }
        var data = {
            "itemId" : this.getAttribute('id'),
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

    function renderTemperature(item, containerId) {
        console.log("Rendering temperature reading for item: " + item.name)

        var data = {
            "widgetId": item.id,
            "name": item.name,
            "value": 0,
            "deviceId": item.deviceId,
            "label": "temperature"
        }

        renderWidgetTemplate("temperatureTemplate", data, item.id, containerId)

        //lets get the initial state for the widget
        forceUpdateDeviceState(item.deviceId);
    }

    function renderWidgetTemplate(templateName, data, itemId, containerId) {
        var template = $('#' + templateName).html();
        Mustache.parse(template);
        var rendered = Mustache.render(template, data);
        appendContainer(rendered, itemId, containerId)
    }

    function forceUpdateDeviceState(deviceId) {
        $.get("/data/state(" + deviceId + ")", function(data){
            if(!isEmpty(data)) {
                handleStateUpdate(data);
            }
        });
    }

    function appendContainer(widgetHtml, widgetId, containerId) {
        console.log("Appending or replacing widget: " + widgetHtml + " for id: " + widgetId);
        if ($("#" + widgetId).length > 0) {
            //widget already exists
        } else {
            var container = $("div[containerId=" + containerId + "]");
            container.append(widgetHtml);
        }
    }

    function isEmpty(str) {
        return (!str || 0 === str.length);
    }

});