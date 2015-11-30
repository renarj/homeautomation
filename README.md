# HomeAutomation
This is the OberaSoftware HomeAutomation software, we hope to eventually provide a better dashboard for all your home automation.

The goal of this project is not to support every single device but instead offer the right integration channels to accept messages from external systems. 

**The main goal of this project is to offer a responsive and user friendly Dashboard integration experience of all your homeautomation appliances.**

## Out of the box support
We support out of the box one integration channel for now and multiple native devices:
* Philips HUE
* NEST thermostat
* Youless energy meter
* ZWave devices

But perhaps most important we offer MQTT Integration, if you have a device that can post a MQTT message we can read it and show the result on our Dashboard.


# MQTT support
The goal for MQTT support is to make sure we can support many different devices without having to build native adapters for each seperate plugin. A good example is that we offer the ability to connect to OpenHab this way, this way we now can display the value of any device that OpenHab supports on our Dashboard.

# Dashboard
We offer a dashboard that allows multiple dashboards with groupings to be created. On each dashboard you can place multiple widgets in a list or grid form. These widgets can be created and configured without opening a single configuration file. The widgets get live updates via a WebSocket that provides live state updates to the dashboard. This all is build using Bootstrap and JQuery to make it responsive and available on mobile devices.
