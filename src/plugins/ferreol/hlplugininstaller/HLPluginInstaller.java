package plugins.ferreol.hlplugininstaller;

import icy.main.Icy;
import icy.plugin.PluginDescriptor;
import icy.plugin.PluginInstaller;
import icy.plugin.PluginRepositoryLoader;
import icy.plugin.PluginUpdater;
import icy.plugin.abstract_.PluginActionable;
import icy.system.thread.ThreadUtil;
import icy.workspace.WorkspaceInstaller;

/**
 * @author ferreol
 *
 */
public class HLPluginInstaller extends PluginActionable {

    @Override
    public void run() {
        if(Icy.getMainInterface().isHeadLess()){
            // wait so we are sure the update process started
            ThreadUtil.sleep(10);
            // then wait until updates are done
            System.out.println("Updating");
            while (PluginUpdater.isCheckingForUpdate() ||  PluginInstaller.isProcessing() || WorkspaceInstaller.isProcessing())
                ThreadUtil.sleep(1);
            if(  Icy.getCommandLinePluginArgs().length!=0){
                for (String arg : Icy.getCommandLinePluginArgs()) {
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
        else{
            System.out.println("HLPluginInstaller is intended to be used in headless mode only");
        }
        System.out.println("HLPluginInstaller is working fine !");
    }

}
