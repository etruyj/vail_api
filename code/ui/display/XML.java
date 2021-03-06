//===================================================================
// XML.java
// 	Formats the OutputFormat Array as an XML table.
//===================================================================

package com.socialvagrancy.vail.ui.display;

import com.socialvagrancy.vail.structures.OutputFormat;

import java.util.ArrayList;

public class XML
{
	public static void display(ArrayList<OutputFormat> output)
	{
		int indent = 0;
		String last_header = "";
		String[] headers;
		String formatted_header;
		String line;

		for(int i=0; i<output.size(); i++)
		{
			headers = output.get(i).header.split(">");

			if(output.get(i).value == null)
			{
				// Header field
				if(headers[headers.length-1].equals(last_header))
				{
					// Closing header
					indent--;
					
					formatted_header = "</" + headers[headers.length-1] + ">";
					Print.line("", formatted_header, indent, false);

					if(headers.length-2>=0)
					{
						last_header = headers[headers.length-2];
					}
					else
					{
						last_header = "";
					}
				}
				else
				{
					// Opening header
					formatted_header = "<" + headers[headers.length-1] + ">";
					Print.line("", formatted_header, indent, false);
					last_header = headers[headers.length-1];
					indent++;
				}
			}
			else
			{
				// Print Value.
				formatted_header = "<" + headers[headers.length-1] + ">";
				line = formatted_header + output.get(i).value;
			
				formatted_header = "</" + headers[headers.length-1] + ">";
				line = line + formatted_header;

				Print.line("", line, indent, false);				
			}
		}
	}
}
