import java.io.*;

public class EcoManager
{
        public static void main(String args[])
        {
                int[] rares = {13858, 13859, 13860, 13861, 13862, 13863, 13864, 13865, 13866, 13867, 13868, 13869, 13932, 13933, 13934, 13935, 13936, 13937, 13938, 13939, 13940, 13941, 13942, 13943, 13888, 13889, 13893, 13894, 13895, 13899, 13900, 13901, 13905, 13906, 13907, 13911, 13912, 13913, 13917, 13918, 13919, 13923, 13924, 13925, 13929, 13930, 13931, 13884, 13886, 13890, 13891, 13892, 13896, 13897, 13898, 13902, 13903, 13904, 13908, 13909, 13910, 13914, 13915, 13916, 13920, 13921, 13922, 13926, 13927, 13928, 13870, 13871, 13872, 13873, 13874, 13875, 13876, 13877, 13878, 13879, 13880, 13881, 13882, 13883, 13944, 13945, 13946, 13947, 13948, 13949, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1053, 1054, 1055, 1056, 1057};
                File charFolder;
                BufferedWriter bw;
                BufferedReader br;
                BufferedWriter tmpwr;
                BufferedReader tmpr;
                boolean overwrite = false;
                String read;
                String dir = "data/characters/";
                double done = 0;
                int percent = 0;
                int percentRounded = 0;
                charFolder = new File(dir);
                if(!charFolder.exists())
                {
                    System.out.println("The directory "+dir+" was not found.");
                    return;
                }
				else
                if(charFolder.list().length == 0)
                {
                    System.out.println("The specified directory is empty.");
                    return;
                }
                String file[] = charFolder.list();
                for(String s : file)
                {
                    try
                    {
						boolean isAdmin = false;
                        File charFile = new File(dir+s);
                        File tmpFile = new File(dir+s+".tmp");
                        tmpFile.createNewFile();
                        br = new BufferedReader(new FileReader(charFile));
                        tmpwr = new BufferedWriter(new FileWriter(tmpFile, true));
                        while((read = br.readLine()) != null)
                        {
                                        if(read.contains("character-rights"))
                                        {
                                                if(read.charAt(read.length() - 1) > '1')
                                                {
                                                        System.out.println("Bank saved for admin "+s.substring(0, s.indexOf(".")));
                                                        isAdmin = true;
                                                }
                                        }

                                        if(read.equals("[ITEMS]") || read.equals("[EQUIPMENT]"))
                                        {
                                                if(!isAdmin)
                                                {
                                                        overwrite = true;
                                                }
                                        }

                                        if(read.equals("[FRIENDS]") || read.equals("[LOOK]"))
                                                overwrite = false;

                                        if(!overwrite)
                                        {
                                                tmpwr.write(read);
                                                tmpwr.newLine();
                                        }
                                        else if(!isAdmin)
                                        {
                                                boolean found = false;
                                                for(int i : rares)
                                                {
                                                        if(read.contains("\t" + i + "\t") || read.contains("\t" + (i + 1) + "\t"))
                                                        {
                                                                found = true;
                                                        }
                                                }
                                                if(!found)
                                                {
                                                        tmpwr.write(read);
                                                        tmpwr.newLine();
                                                }
                                        }
                                }
                                tmpwr.flush();
                                tmpwr.close();
                                br.close();

                                charFile.delete();

                                bw = new BufferedWriter(new FileWriter(charFile, true));
                                tmpr = new BufferedReader(new FileReader(tmpFile));

                                while((read = tmpr.readLine()) != null)
                                {
                                        bw.write(read);
                                        bw.newLine();
                                }

                                bw.flush();
                                bw.close();
                                tmpr.close();
                                tmpFile.delete();

                                done++;

                                if((percent = (int)(done / file.length * 100)) % 5 < 5 && percent - percent % 5 != percentRounded)
                                        System.out.println((percentRounded = (int)(percent - percent % 5))+"%");
                        }
                        catch(IOException Ioe)
                        {
                                Ioe.printStackTrace();
                        }
                }
                System.out.println("Finished!");
        }
}