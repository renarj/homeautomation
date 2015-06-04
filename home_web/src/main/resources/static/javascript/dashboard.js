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
        };
        var jsonData = JSON.stringify(container);

        $.ajax({url: "/ui/containers", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            var containerId = data.id;
            console.log("Posted Container successfully, id: " + containerId);

            $('#containerModal').modal('hide');

            renderContainerById(containerId);
        }})

    })
});