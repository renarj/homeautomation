$(document).ready(function() {
	console.log("Document ready");

 	$(document).on("click", ".save", function (event) {
        event.preventDefault();

        console.log("Trying to save");

		var xml = Blockly.Xml.workspaceToDom(workspace);
		var xml_text = Blockly.Xml.domToPrettyText(xml);
		console.log("XML: " + xml_text);

    });


 	$(document).on("click", ".load", function (event) {
        event.preventDefault();

        console.log("Trying to Load");

        var xml_text = $("#xmlSource").val();

        var xml = Blockly.Xml.textToDom(xml_text);
		Blockly.Xml.domToWorkspace(workspace, xml);
    });


});