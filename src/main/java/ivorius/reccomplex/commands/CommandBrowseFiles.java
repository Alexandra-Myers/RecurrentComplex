/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.commands;

import ivorius.reccomplex.RCConfig;
import ivorius.reccomplex.RecurrentComplex;
import ivorius.reccomplex.files.RCFileTypeRegistry;
import ivorius.reccomplex.utils.ServerTranslations;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by lukas on 03.08.14.
 */
public class CommandBrowseFiles extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return RCConfig.commandPrefix + "files";
    }

    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender)
    {
        return ServerTranslations.usage("commands.rcfiles.usage");
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args)
    {
        openFile(RCFileTypeRegistry.getBaseDirectory(), RecurrentComplex.logger);
    }

    /**
     * From GuiScreenResourcePacks
     */
    public static void openFile(File file, Logger logger)
    {
        String s = file.getAbsolutePath();

        if (Util.getOSType() == Util.EnumOS.OSX)
        {
            try
            {
                logger.info(s);
                Runtime.getRuntime().exec(new String[] {"/usr/bin/open", s});
                return;
            }
            catch (IOException ioexception1)
            {
                logger.error("Couldn\'t open file", ioexception1);
            }
        }
        else if (Util.getOSType() == Util.EnumOS.WINDOWS)
        {
            String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", s);

            try
            {
                Runtime.getRuntime().exec(s1);
                return;
            }
            catch (IOException ioexception)
            {
                logger.error("Couldn\'t open file", ioexception);
            }
        }

        boolean flag = false;

        try
        {
            Class<Desktop> oclass = (Class<Desktop>) Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, file.toURI());
        }
        catch (Throwable throwable)
        {
            logger.error("Couldn\'t open link", throwable);
            flag = true;
        }

        if (flag)
        {
            logger.info("Opening via system class!");
            Sys.openURL("file://" + s);
        }
    }
}
