package plugins.ferreol.icyhlplugininstaller;

import java.util.ArrayList;

import icy.main.Icy;
import icy.plugin.PluginDescriptor;
import icy.plugin.PluginInstaller;
import icy.plugin.PluginRepositoryLoader;
import icy.plugin.PluginUpdater;
import icy.plugin.abstract_.PluginActionable;
import icy.system.thread.ThreadUtil;
import icy.update.IcyUpdater;
import icy.workspace.WorkspaceInstaller;

/**
 * @author ferreol
 *
 */
public class IcyHLPluginInstaller extends PluginActionable {

    @Override
    public void run() {
        if(Icy.getMainInterface().isHeadLess()){
            // wait so we are sure the update process started
            ThreadUtil.sleep(10);
            // then wait until updates are done
            System.out.println("Updating");
            while (PluginUpdater.isCheckingForUpdate() ||  PluginInstaller.isProcessing() || WorkspaceInstaller.isProcessing())
                ThreadUtil.sleep(1);

            // wait for repository loader is loaded
            PluginRepositoryLoader.waitLoaded();
            if(  Icy.getCommandLinePluginArgs().length!=0){
                if (Icy.getCommandLinePluginArgs()[0].equalsIgnoreCase("--all")){
                    IcyUpdater.checkUpdate(true);
                    ThreadUtil.sleep(10);
                    ArrayList<PluginDescriptor> plugList = PluginRepositoryLoader.getPlugins();
                    for (PluginDescriptor desc : plugList) {
                        System.out.println("Installing :"+desc.getName());
                        // install  plugin
                        if(!desc.getName().contains("Matlab")){
                            PluginInstaller.install(desc, false);
                            while (PluginUpdater.isCheckingForUpdate() ||  PluginInstaller.isProcessing() || PluginInstaller.isInstalling())
                                ThreadUtil.sleep(1);
                        }
                    }
                }else{
                    for (String arg : Icy.getCommandLinePluginArgs()) {

                        if (Icy.getCommandLinePluginArgs()[0].equalsIgnoreCase("--update")){
                            IcyUpdater.checkUpdate(true);
                            ThreadUtil.sleep(10);
                        }else{
                            System.out.println("Installing :"+arg);
                            // wait for repository loader is loaded
                            PluginRepositoryLoader.waitLoaded();
                            // get  plugin descriptor
                            PluginDescriptor desc = PluginRepositoryLoader.getPlugin(arg);
                            // install  plugin
                            PluginInstaller.install(desc, false);
                            while (PluginUpdater.isCheckingForUpdate() ||  PluginInstaller.isProcessing() || PluginInstaller.isInstalling())
                                ThreadUtil.sleep(1);
                        }
                    }
                }
            }
        }
        else{
            System.out.println("IcyHLPluginInstaller is intended to be used in headless mode only");
        }
    }

}
