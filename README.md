# IcyHLPluginInstaller
IcyHLPluginInstaller is a small [Icy plugin](http://icy.bioimageanalysis.org/). It will update Icy and install new plugin in headless (command line) mode when the GUI cannot be launch (eg on serveur). 

Syntax: 
```bash
java -jar icy.jar -hl -x plugins.ferreol.icyhlplugininstaller CLASSNAME
```
where 'CLASSNAME' is the ClassName of the desired plugin. This ClassName can be found in the "See technical details" link on  the plugin webpage.
If 'CLASSNAME' is empty, it will only update Icy and the existing plugin. 

Exemple: 
```bash
java -jar icy.jar -hl -x plugins.ferreol.icyhlplugininstaller plugins.adufour.ezplug.EzPlug
```

The jar file of IcyHLPluginInstaller is stored in the jar branch and can be downloaded in automatic script to build icy as in https://hub.docker.com/r/ferreol/icy/~/dockerfile/ 
