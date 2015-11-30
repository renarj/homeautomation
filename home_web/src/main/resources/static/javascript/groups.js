$(document).ready(function() {
    var sortables = $(".sortable");
    sortables.each(function(index) {
        var container = $(this);

        container.sortable({

            connectWith: ".sortable",
            update: function( event, ui ) {
                console.log("Reordered inside UL");
            },
            receive: function( event, ui ) {
                console.log("Changed ul");
            }
        });
        container.disableSelection();
    });

    $(document).on("click", ".removeGroup", function (event) {
        event.preventDefault();
        var groupId = this.getAttribute('groupId');
        console.log("Removing group: " + groupId);

        $.ajax({url: "/groups/groups(" + groupId + ")", type: "DELETE", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Removed Group successfully");

            location.reload(true);
        }, error: function(data) {
            console.log("Failed: " + data);
        }});
    });

    $.get("/data/controllers", function(data){
        if(!isEmpty(data)) {
            var controllerDiv = $("#controllers");

            $.each(data, function (i, ci) {
                controllerDiv.append("<a  class=\"list-group-item select\" href=\"#\" controllerId=" + ci.controllerId + ">" + ci.controllerId + "</a>");
            })
        }
    });

    $(document).on("click", ".select", function (event) {
        event.preventDefault();

        var controllerId = this.getAttribute('controllerId');
        $(".active").removeClass("active");
        $(this).addClass("active");
        $("#groupInfo").empty();
        console.log("Selecting Controller: " + controllerId);

        var groupsDiv = $("#groupList");
        groupsDiv.empty();

        renderAndAppend("groupsTemplate", "{}", "groupList");

        $.get("/groups/controller(" + controllerId + ")", function(data) {
            if (!isEmpty(data)) {

                $.each(data, function (i, ci) {
                    var data = {
                        "name": ci.name,
                        "groupId": ci.id
                    };


                    renderAndAppend("groupListItem", data, "groups");
                })
            }
        });

        var deviceList = $("#selectableDeviceList");
        deviceList.empty();
        $("#controllerId").val(controllerId);

        $.get("/data/controllers(" + controllerId + ")/devices", function(data) {
            $.each(data, function (i, ci) {
                var name = ci.item.name;
                var plugin = ci.item.pluginId;

                deviceList.append("<li class=\"list-group-item\" deviceId=\"" + ci.item.id + "\">" + name +"(" + plugin + ")</li>");
            });
        });

    });

    $(document).on("click", ".selectGroup", function (event) {
        event.preventDefault();

        var groupId = this.getAttribute('groupId');
        var groupName = this.getAttribute('groupName');
        console.log("Selecting Group: " + groupId);

        var groupInfoDiv = $("#groupInfo");
        groupInfoDiv.empty();

        var data = {
            "groupId": groupId,
            "name" : groupName
        };
        renderAndAppend("groupInfoTemplate", data, "groupInfo");

        $.get("/groups/groups(" + groupId + ")/devices", function(data) {
            if (!isEmpty(data)) {

                $.each(data, function (i, ci) {
                    var data = {
                        "name": ci.name,
                        "id": ci.id
                    };

                    renderAndAppend("deviceTemplate", data, "deviceList");
                })
            }
        });
    });



    $("#groupForm").submit(function(event) {
        event.preventDefault();

        console.log("Group form submitted");

        var name = $("#groupName").val();
        var controllerId = $("#controllerId").val();
        var groupDeviceContainer = $("#groupDevices");

        var devices = [];
        groupDeviceContainer.children("li").each(function(index) {
            var deviceId = $(this).attr("deviceId");
            console.log("Group has device: " + deviceId);
            devices.push(deviceId);
        });

        var groupJson = {
            "name" : name,
            "controllerId" : controllerId,
            "deviceIds" : devices
        };

        var jsonData = JSON.stringify(groupJson);

        $.ajax({url: "/groups/", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Posted Group successfully");

            $('#dataModal').modal('hide');

            location.reload(true);
        }})
    });

    function renderTemplate(templateName, data) {
        var template = $('#' + templateName).html();
        Mustache.parse(template);
        return Mustache.render(template, data);
    }

    function renderAndAppend(templateName, data, elementId) {
        $("#" + elementId).append(renderTemplate(templateName, data));
    }

    function isEmpty(str) {
        return (!str || 0 === str.length);
    }

});

