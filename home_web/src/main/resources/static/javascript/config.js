$(document).ready(function() {
    function toggleChevron(e) {
        $(e.target)
            .prev('.panel-heading')
            .find('i.indicator')
            .toggleClass('glyphicon-chevron-down glyphicon-chevron-right');
    }
    $('#accordion').on('hidden.bs.collapse', toggleChevron);
    $('#accordion').on('shown.bs.collapse', toggleChevron);

    $(document).on("click", ".addItem", function () {
        var deviceId = $(this).data('id');
        $(".modal-body #deviceId").val( deviceId );
    });

    $("#createUIItemForm").submit(function(event) {
        console.log("Creating UI Item")
        event.preventDefault();

        var name = $("#itemName").val();
        var description = $("#itemDescription").val();
        var container = $("#containerList").find('option:selected').val();
        var widget = $("#widgetList").find('option:selected').val();
        var deviceId = $("#deviceId").val();

        console.log("Creating ui item namne: " + name);
        console.log("Creating ui item description: " + description);
        console.log("Creating ui item container: " + container);
        console.log("Creating ui item widget: " + widget);

        var item = {
            "name" : name,
            "description" : description,
            "uiType" : widget,
            "containerId" : container,
            "deviceId" : deviceId
        }
        var jsonData = JSON.stringify(item);

        $.ajax({url: "/ui/items", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Posted UI Item successfully");

            $('#dataModal').modal('hide')
        }})
    })
});