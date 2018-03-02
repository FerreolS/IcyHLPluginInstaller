/**
 *  Copyright (C) 2017  Ferreol Soulez ferreol.soulez@univ-lyon1.fr
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  */

package plugins.ferreol.icyhlplugininstaller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    static List<String> hlBuggyPlugin = Arrays.asList("Matlab communicator","Matlab X server","Invert","Generate a bug");

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
                        // if((!desc.getName().contains("Matlab"))&&(!desc.getName().contains("Invert"))){
                        if (!hlBuggyPlugin.contains(desc.getName())){
                            if( !desc.isInstalled()){
                                PluginInstaller.install(desc, false);
                                while (PluginUpdater.isCheckingForUpdate() ||  PluginInstaller.isProcessing() || PluginInstaller.isInstalling())
                                    ThreadUtil.sleep(1);
                            }
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
                            if( !desc.isInstalled()){
                                PluginInstaller.install(desc, false);
                                while (PluginUpdater.isCheckingForUpdate() ||  PluginInstaller.isProcessing() || PluginInstaller.isInstalling())
                                    ThreadUtil.sleep(1);
                            }
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
