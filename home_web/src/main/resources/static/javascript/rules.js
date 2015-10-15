$(document).ready(function() {
    $(document).on("click", ".deleteRule", function(event) {
        event.preventDefault();

        var ruleId = $("#editRule").attr("ruleId");
        console.log("Removing Rule: " + ruleId);

        $.ajax({url: "/rules/(" + ruleId + ")", type: "DELETE", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Removed Rule successfully");

            location.reload(true);
        }});
    });

    $(document).on("click", ".saveRule", function (event) {
        event.preventDefault();

        console.log("Trying to save");

        var controllerId = $(this).attr('controllerId');

        var xml = Blockly.Xml.workspaceToDom(workspace);
        var xml_text = Blockly.Xml.domToPrettyText(xml);
        console.log("XML: " + xml_text);


        var rule = {
            "controllerId" : controllerId
        };

        var ruleId = $("#editRule");
        if(ruleId) {
            rule["id"] = ruleId.attr("ruleId");
        }


        rule["properties"] = {
            "Blockly" : xml_text
        };


        var jsonData = JSON.stringify(rule);

        $.ajax({url: "/rules/", type: "POST", data: jsonData, dataType: "json", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Posted Rule successfully");

            location.reload(true);
        }, error: function(xhr, status, error) {
            console.log("Request error: " + error + " reason: " + xhr.responseText);

            var responseData = xhr.responseText;
            var json = JSON.parse(responseData);

            console.log("Error parsing: " + json.message);

            $("#errorReason").text("Error processing blockly diagram: " + json.message);
            $("#errorModal").modal('toggle');
        }})
    });

    $(document).on("click", ".resetRule", function (event) {
        event.preventDefault();

        console.log("Clearing workspace")
        Blockly.mainWorkspace.clear();
    });
});

var blocklyArea = document.getElementById('blocklyArea');
var blocklyDiv = document.getElementById('blocklyDiv');

var workspace = Blockly.inject(blocklyDiv,
    {
        media: 'media/',
        toolbox: document.getElementById('toolbox'),
        trashcan: false,
        scrollbars: false,
        sound: false
    }
);

var itemToolbox = $("#ItemToolbox");
var groupToolbox = $("#GroupToolbox");
var virtualToolbox = $("#VirtualToolbox");

var onresize = function(e) {
    // Compute the absolute coordinates and dimensions of blocklyArea.
    var element = blocklyArea;
    var x = 0;
    var y = 0;
    do {
        x += element.offsetLeft;
        y += element.offsetTop;
        element = element.offsetParent;
    } while (element);
    // Position blocklyDiv over blocklyArea.
    blocklyDiv.style.left = x + 'px';
    blocklyDiv.style.top = y + 'px';
    blocklyDiv.style.width = blocklyArea.offsetWidth + 'px';
    blocklyDiv.style.height = blocklyArea.offsetHeight + 'px';
};
window.addEventListener('resize', onresize, false);
onresize();


$.when(
    $.get("/data/devices", function(data) {
        $.each(data, function(i, row) {
            console.log("Found a device: " + row.item.id)
            var pluginId = row.item.pluginId;
            var deviceId = row.item.deviceId;
            var blockId = "Device." + row.item.id;
            var name = "Plugin: " + pluginId + " Device: " + deviceId;

            createItemBlock(blockId, name);

            appendToToolbox(itemToolbox, blockId);
        })
    }),
    $.get("/groups/", function(data) {
        $.each(data, function(i, row) {
            console.log("Found a group: " + row.id)
            var groupId = row.id;
            var name = row.name;
            var blockId = "Group." + groupId;

            createItemBlock(blockId, name);

            appendToToolbox(groupToolbox, blockId);
        })
    }),
    $.get("/virtualitems/", function(data) {
        $.each(data, function(i, row) {
            console.log("Found a item: " + row.id)
            var groupId = row.id;
            var name = row.name;
            var blockId = "Item." + groupId;

            createItemBlock(blockId, name);

            appendToToolbox(virtualToolbox, blockId);
        })
    })
).then(function(resp1, resp2){
        console.log("Devices and Groups are loaded, checking if editing existing rule");
        loadEditRule();
});

function loadEditRule() {
    var editDiv = $("#editRule");
    if (editDiv) {
        console.log("Editing existing rule");

        //var xml_text = $("#xmlSource").val();
        var rule = editDiv.html();
        console.log("Loading rule: " + rule);

        var xml = Blockly.Xml.textToDom(rule);
        Blockly.Xml.domToWorkspace(workspace, xml);
    }
}


function createItemBlock(blockId, fieldName) {
    Blockly.Blocks[blockId] = {
        init: function() {
            this.appendDummyInput()
                .appendField(fieldName);
            this.setInputsInline(true);
            this.setOutput(true, "String");
            this.setColour(330);
            this.setTooltip('');
            this.setHelpUrl('www.oberasoftware.com/haas');
        }
    };
}

function appendToToolbox(categoryElement, blockId) {
    categoryElement.append("<block type=\"" + blockId + "\"></block>");

    workspace.updateToolbox(document.getElementById('toolbox'));
}