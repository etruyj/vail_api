package com.socialvagrancy.vail.ui;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.utils.VailController;
import com.socialvagrancy.vail.ui.display.Display;

import java.util.ArrayList;

public class VailAPI
{
	VailController controller;

	public VailAPI()
	{
		controller = new VailController();
	}

	public void execute(String ip, String command, String option1, String option2, String option3, String option4, boolean boolean_flag, String outputFormat)
	{
		ArrayList response;

		switch(command)
		{
            case "capacity-summary":
                controller.getCapacitySummary(ip);
                break;
			case "clear-cache":
				controller.clearCache(ip);
				break;
			case "configure":
			case "configure-sphere":
				Display.print(controller.configureSphere(ip, option4));
				break;
			case "create-bucket":
				Display.output(controller.createBucket(ip, option2, option1), outputFormat);
				break;
			case "create-group":
				Display.print(controller.createGroup(ip, option2, option1));
				break;
			case "create-user":
				Display.print(controller.createUser(ip, option1, option3));
				break;
            case "enable-veeam":
                Display.print(controller.enableVeeam(ip, option2));
                break;
            case "fetch-config":
				Display.output(controller.fetchConfiguration(ip), outputFormat, option4);
				break;
			case "help":
				Display.printHelp("../lib/help/basic.txt");
				Display.printHelp("../lib/help/advanced.txt");
				break;
			case "list-accounts":
				Display.output(controller.listAccounts(ip), outputFormat);
				break;
			case "list-buckets":
				if(outputFormat.equals("raw"))
				{
					Display.output(controller.listBuckets(ip), outputFormat);
				}
				else
				{
					Display.output(controller.listBucketSummary(ip, option1), outputFormat);
				}
				break;
			case "list-groups":
				Display.output(controller.listGroups(ip, option1), outputFormat);
				break;
			case "list-storage":
				Display.output(controller.listStorage(ip), outputFormat);
				break;
			case "list-users":
				response = controller.listUsers(ip, option1, boolean_flag);
				Display.output(response, outputFormat);
				break;
			case "min-iam-policy":
				controller.findMinIAMPermissions(ip);
				break;
			case "update-owner":
				controller.updateOwner(ip, option2, option1);
				break;
			case "default":
				Display.print("Invalid command [" + command + "] selected. Please used -c help for a list of valid commands.");
				break;
		}
	}

	public boolean login(String ip, String username, String password)
	{
		return controller.login(ip, username, password);
	}

	public static void main(String[] args)
	{
		ArgParser aparser = new ArgParser();

		if(aparser.parseArgs(args))
		{
			VailAPI ui = new VailAPI();
		
			if(aparser.helpRequested())
			{
				Output.printHelp("../lib/help/options.txt");
			}
			else if(aparser.getCommand().substring(0, 4).equals("help"))
			{
				ui.execute("",  aparser.getCommand(), "", "", "", "", false, "");
			}
			else if(ui.login(aparser.getIP(), aparser.getUsername(), aparser.getPassword()))
			{
				ui.execute(aparser.getIP(), aparser.getCommand(), aparser.getOption1(), aparser.getOption2(), aparser.getOption3(), aparser.getOption4(), aparser.getBooleanFlag(), aparser.getOutputFormat());
			}
			else
			{
				Output.print("Unable to login with specified credentials.");
			}
		}
		else
		{
			Output.print("Invalid input entered. Please use --help to see a list of valid commands.");
		}
	}
}

