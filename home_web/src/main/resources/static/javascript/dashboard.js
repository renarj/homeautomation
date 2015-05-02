$(document).ready(function() {
    $(document).on("click", ".addContainer", function () {
        var parentContainerId = $(this).data('id');
        $(".modal-body #parentContainerId").val( parentContainerId );
    });

    $("#createContainerForm").submit(function(event){
        event.preventDefault();

        var name = $("#containerName").val();
        var parentContainer = $("#parentContainerId").val();

        var container = {
            "name" : name,
            "parentContainerId" : parentContainer
        }
        var jsonData = JSON.stringify(container);

        $.ajax({url: "/ui/containers", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Posted Container successfully");

            $('#containerModal').modal('hide')
        }})

    })

    $('div[containerId]').each(function(i,el) {

        var containerId = el.getAttribute("containerId");
        console.log(containerId);

        $.get("/ui/containers/" + containerId + "/items", function(data) {
            $.each(data, function(i, item) {
                renderWidget(containerId, item);
            })

        })

    });

    function renderWidget(containerId, item) {
        console.log("Rendering widget: " + item.id);
        var widgetType = item.uiType;
        switch (widgetType.toLowerCase()) {
            case "switch":
                renderSwitch(item);
                break;
            case "dimmer":
                break;
            case "temperature":
                renderTemperature(containerId, item);
                break;
            default:
                console.log("Unsupported widget type: " + widgetType + " for item: " + item.name);
        }
    }

    function renderSwitch(item) {
        console.log("Rendering switch for item: " + item.name)


    }

    function renderTemperature(containerId, item) {
        console.log("Rendering temperature reading for item: " + item.name)

        var deviceId = item.deviceId;

        $.get("/data/state(" + deviceId + ")", function(data){
            console.log("Got response data: " + data);
            var stateItems = data.stateItems;
            console.log("Retrieved " + stateItems.length + " for device: " + deviceId);

            $.each(stateItems, function(i, stateItem) {
                console.log("Found state label: " + stateItem.label);
                if(stateItem.label == "temperature") {
                    console.log("We have temperature of: " + stateItem.value.value);

                    var temperature = stateItem.value.value;

                    var template = $('#temperatureTemplate').html();
                    Mustache.parse(template);
                    var rendered = Mustache.render(template, {"widgetId": item.id, "name": item.name, "value": stateItem.value.value});
                    appendContainer(rendered, item.id, containerId)
                }
            })
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
});