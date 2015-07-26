Blockly.Blocks['rule'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("Rule Name")
            .appendField(new Blockly.FieldTextInput(""), "rule_name");
        this.appendStatementInput("ruleTrigger")
            .appendField("Trigger");
        this.appendStatementInput("ruleStatement")
            .appendField("Conditions");
        this.setColour(180);
        this.setTooltip('');
    }
};

Blockly.Blocks['triggerDevice'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("Device Change");
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setColour(240);
        this.setTooltip('');
    }
};

Blockly.Blocks['onoff'] = {
    init: function() {
        this.appendDummyInput()
            .appendField(new Blockly.FieldDropdown([["on", "on"], ["off", "off"]]), "state");
        this.setInputsInline(true);
        this.setOutput(true, "String");
        this.setColour(330);
        this.setTooltip('');
    }
};
Blockly.Blocks['movement'] = {
    init: function() {
        this.appendDummyInput()
            .appendField(new Blockly.FieldDropdown([["detected", "detected"], ["not detected", "not detected"]]), "NAME");
        this.setInputsInline(true);
        this.setOutput(true, "String");
        this.setColour(330);
        this.setTooltip('');
    }
};


Blockly.Blocks['devicevalue'] = {
    init: function() {
        this.appendValueInput("item")
            .setCheck("String")
            .appendField("Device:");
        this.appendDummyInput()
            .appendField("Value:")
            .appendField(new Blockly.FieldDropdown([["power", "power"], ["temperature", "temperature"], ["movement", "movement"], ["luminance", "luminance"]]), "label");
        this.setInputsInline(true);
        this.setOutput(true);
        this.setColour(255);
        this.setTooltip('Returns the value of a Device');
    }
};
Blockly.Blocks['switch_item'] = {
    init: function() {
        this.appendValueInput("item")
            .setCheck("String")
            .appendField("Switch Device:");
        this.appendDummyInput()
            .appendField(new Blockly.FieldDropdown([["on", "on"], ["off", "off"]]), "state");
        this.setInputsInline(true);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setColour(165);
        this.setTooltip('Switch a device on or off');
    }
};
