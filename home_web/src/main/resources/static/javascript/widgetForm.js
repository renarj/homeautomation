$(document).ready(function() {
    $(document).on("click", ".addWidget", function () {
        var containerId = $(this).data('id');
        $(".modal-body #containerId").val( containerId );

        loadControllers();
    });

    $(document).on("click", ".removeWidget", function (event) {
        event.preventDefault();
        var widgetId = this.getAttribute('widgetId');

        $.ajax({url: "/ui/items/" + widgetId, type: "DELETE", dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Removed Widget successfully");
        }});

        $("#" + widgetId).remove();
    });

    $(document).on("click", ".removeContainer", function (event) {
        event.preventDefault();
        var containerId = this.getAttribute('containerId');

        $.ajax({url: "/ui/containers/" + containerId, type: "DELETE", dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Removed container successfully");
        }});

        $("#" + containerId).remove();
    });




    function loadControllers() {
        $.get("/data/controllers", function(data){
            if(!isEmpty(data)) {
                var list = $("#controllerList");
                list.empty();

                if(data.length > 1) {
                    list.append(new Option("", ""));
                }

                $.each(data, function (i, ci) {
                    list.append(new Option(ci.controllerId, ci.controllerId));
                })

                var selectedController = list.find('option:selected').val();
                loadPlugins(selectedController);
            }
        });
    }

    $("#controllerList").change(function() {
        var selectedController = $("#controllerList").find('option:selected').val();
        loadPlugins(selectedController);
    })

    function loadPlugins(controllerId) {
        console.log("Retrieving plugins for controller: " + controllerId);
        $.get("/data/controllers(" + controllerId + ")/plugins", function(data){
            if(!isEmpty(data)) {
                var list = $("#pluginList");
                list.empty();
                list.append(new Option("", ""));

                $.each(data, function (i, ci) {
                    list.append(new Option(ci.name, ci.pluginId));
                })
            }
        })
    }

    $("#pluginList").change(function() {
        var selectedController = $("#controllerList").find('option:selected').val();
        var selectedPlugin = $("#pluginList").find('option:selected').val();

        console.log("Retrieving devices for controller: " + selectedController + " and plugin: " + selectedPlugin);
        $.get("/data/controllers(" + selectedController + ")/plugins("+selectedPlugin+")/devices", function(data){
            if(!isEmpty(data)) {
                var list = $("#deviceList");
                list.empty();
                list.append(new Option("", ""));

                $.each(data, function (i, deviceRow) {
                    var item = deviceRow.item;
                    list.append(new Option(item.name, item.id));
                })
            }
        })
    })

    $("#widgetList").change(function () {
        var widgetType = this.value;
        if(widgetType == "label") {
            $("#widgetValueTypeDiv").removeClass("hide");
            $("#widgetUnitTypeDiv").removeClass("hide");
        } else {
            $("#widgetValueTypeDiv").addClass("hide");
            $("#widgetUnitTypeDiv").addClass("hide");
        }
    });

    $("#createUIItemForm").submit(function(event) {
        console.log("Creating UI Item")
        event.preventDefault();

        var name = $("#itemName").val();
        var description = $("#itemDescription").val();
        var container = $("#containerId").val();
        var widget = $("#widgetList").find('option:selected').val();
        var deviceId = $("#deviceList").find('option:selected').val();


        console.log("Creating ui item name: " + name);
        console.log("Creating ui item description: " + description);
        console.log("Creating ui item container: " + container);
        console.log("Creating ui item widget: " + widget);

        var item = {
            "name" : name,
            "description" : description,
            "uiType" : widget,
            "containerId" : container,
            "deviceId" : deviceId
        };
        if(widget == "label") {
            var label = $("#widgetLabel").find('option:selected').val();
            var unit = $("#widgetLabelUnitType").find('option:selected').text();
            console.log("We have a label: " + label + " and unit: " + unit);

            item["properties"] = {
                "label" : label,
                "unit" : unit
            }
        }

        var jsonData = JSON.stringify(item);

        $.ajax({url: "/ui/items", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Posted UI Item successfully");

            $('#dataModal').modal('hide')

            renderWidget(container, data);
        }})
    })

    function isEmpty(str) {
        return (!str || 0 === str.length);
    }

});