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
});