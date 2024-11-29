package com.example.abslouteloader;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class LinkerLoaderController {
    //JAVAFX
    @FXML
    public TextArea area1 = new TextArea();
    public Button ok;
    public TextField textfield;
    public Label label1;
    //-----------------------------
    String[][] mem = new String[2050][17]; //memory
    ArrayList<String> trecord = new ArrayList<>(); // t record
    ArrayList<String> prog_names=new ArrayList<>(); //length = number of prog.
    ArrayList<String> progNames_D=new ArrayList<>(); //prgram names +def
    ArrayList<String> length=new ArrayList<>(); //length of each prog. same index as prog_names
    String temp,location,temp2,tempLoc,temp3,temp4,temp5,endOfprog,x,startT, line2,startT2,startM,lengthM,op,symbol,temp6,temp7,temp8;
    int stop; //index of end of memory
    int t,a,b;
    HashMap<String, String> map = new HashMap<>(); //extrernal symbol table
    StringBuilder stemp = new StringBuilder();

    //================================================================================
    public void showdata() throws IOException {
        location = textfield.getText();
        if (textfield.getText().isEmpty()) { //3ashan law mdkhlsh value fl textfield my3mlsh error
            System.out.println(textfield.getText());
            label1.setText("Enter a memory location!");
            location = textfield.getText();
        }
        else {

          //  loc = Integer.parseInt(location);

            FileInputStream file = new FileInputStream("in.txt");
            Scanner sic = new Scanner(file);
            String line;

            while (sic.hasNextLine()) {
                line = sic.nextLine();
                if (line.charAt(0) == 'H') {
                    temp = line.substring(1, 7); //program name
                    temp = temp.replaceAll("X", ""); //delete X's in program name
                    prog_names.add(temp); //add in arryaylist prog_names
                    progNames_D.add(temp); //add in arryaylist progNames_D
                    length.add(line.substring(13, 19)); //length of program in arraylist length

                }
                if (line.charAt(0) == 'D') {
                    for (int i = 1; i < line.length(); i += 6)  //add definition and its location in arryalist progNames_D
                    {
                        temp = line.substring(i, i + 6);
                        temp = temp.replaceAll("X", ""); //delete X's in definitions and doesn't affect location of definition
                        progNames_D.add(temp);
                    }
                }

            }
            sic.close(); //close file

    //we have arraylist with each program's name
            //arraylist with ech program's length
            //arraylist with all programs names, defintions and definitions location (kol program wara b3d hagto kolaha byfslo el program name ely warah)
//-============================================================================================================
            //extrenal symbol table
            FileWriter extab = new FileWriter("EXTAB.txt"); //output file
            BufferedWriter EXSTAB = new BufferedWriter(extab);
            EXSTAB.write("Control section\tSymbol\tAddress\tLength");
            EXSTAB.newLine();
            int l = prog_names.size(); //l -- > number of programs
            int i = 0; //counter l list progNames_D
            temp = progNames_D.get(i); //awel program name

            for (int j = 0; j < l; j++)  //loop 7asb number of programs
            {

                if (temp == prog_names.get(j))
                {
                    if (j == 0)  //law awel prog bn add el location el given
                    {
                        tempLoc = Integer.toHexString(Integer.parseInt(location, 16)); //b save location 3ashan ely waraya yst3mlo
                        map.put(temp, tempLoc); //bagahez map 3ashan ast3ml el values b3d keda
                        temp2 = temp + "\t\t\t" + location + "\t" + length.get(0); //line 7a7oto fl output file
                    }
                    else //law ay program tany b add starting location ely ablya + length bt3o
                    {
                        //temp3-->location ely ablaya bs el length =starting addres
                        String temp3 = Integer.toHexString(Integer.parseInt(tempLoc, 16) + Integer.parseInt(length.get(j - 1), 16)); //value in string is hexa

                        map.put(temp, temp3);//bagahez map 3ashan ast3ml el values b3d keda
                        temp2 = temp + "\t\t\t" + temp3 + "\t" + length.get(j);//line 7a7oto fl output file

                        tempLoc = temp3;
                    }
                    EXSTAB.write(temp2); //write fl file
                    EXSTAB.newLine();
                    i++; //inc progNames_D
                    while (i < progNames_D.size()) //temp=def symbol
                    {

                        temp = progNames_D.get(i); //value f progNames_D (progname-defintion-location)
                        i++;
                        if (j < l - 1)
                        {
                            if (temp == prog_names.get(j + 1))  // law temp = progname exit bs dec i 3ashan akhosh fl if tany fo2
                            {
                                i--;
                                break;
                            }
                        }
                        //law msh b program name tb2a defintion fa ely waraha el location
                        temp3 = progNames_D.get(i); //temp 3 =def loc
                        i++;
                        //lazem a add el starting address bt3 el program 3ala el loaction -->temp 4
                        temp4 = Integer.toHexString(Integer.valueOf(temp3, 16) + Integer.valueOf(tempLoc, 16)); //def loc updated
                        map.put(temp, temp4);
                        temp5 = "\t\t" + temp + "\t" + temp4;

                        EXSTAB.write(temp5);
                        EXSTAB.newLine();
                    }
                }
            }
            //akher starting addres + length akher program = end of memory
            endOfprog = Integer.toHexString(Integer.parseInt(tempLoc, 16) + Integer.parseInt(length.get(length.size() - 1), 16)); //end of memory

            //print map
//        for(Map.Entry mp: map.entrySet()){
//            System.out.println(mp.getKey()+ " "+mp.getValue());
//        }
            EXSTAB.close();
//-============================================================================================================
// MEMORY
            for (int k = 0; k <= Integer.parseInt(location, 16) / 16; k++)  //FILL MEMORY WIH x CLOSE TO NO OF LOCATIONS (NOT CUURATE)
            {
                for (int j = 0; j < 17; j++) {
                    mem[k][j] = "x";
                }
            }

            //set row 0 (ADDRESS 0-F)
            for (int k = 0; k < 17; k++) {
                if (k == 0)
                    mem[0][k] = "Address";
                else {
                    if (k > 10)
                        mem[0][k] = Integer.toHexString(Integer.valueOf(k - 1));
                    else
                        mem[0][k] = Integer.valueOf(k - 1).toString();
                }
            }
            //-============================================================================================================

            //set col 0 (FROM STARTING ADDRESS TO END ADDRESS & GET ACCURATE I OF END OF MEMORY TO PRINT IT ACCURATE)
            t = Integer.parseInt(location, 16);//STARTING ADDRESS
            for (int k = 0; k <= Integer.parseInt(location, 16) / 16; k++) {
                temp2 = Integer.toHexString(t + 16 * k); // start+16* K

                if (Integer.parseInt(temp2, 16) > Integer.parseInt(endOfprog, 16))  //compare end decimal w/ current row value
                {
                    stop = k;
                    break;
                } else {
                    //3ashan akaly el address fl memory 4 digits
                    if (temp2.length() == 2)
                        mem[k + 1][0] = "00" + temp2;
                    else if (temp2.length() == 3)
                        mem[k + 1][0] = "0" + temp2;
                    else if (temp2.length() == 1)
                        mem[k + 1][0] = "000" + temp2;
                    else
                        mem[k + 1][0] = temp2;
                }
            }


//-============================================================================================================
 //T RECORD
            FileInputStream file1 = new FileInputStream("in.txt"); //BAFT7 EL FILE TANY
            Scanner sic1 = new Scanner(file1);
            int f = 0; //counter (index l program name)
            while (f < prog_names.size())  //LOOP 7ASB programs number
            {
                line = sic1.nextLine();
                if (line.charAt(0) == 'H')
                {
                    f++;
                    temp = line.substring(1, 7); //progname
                    temp = temp.replaceAll("X", ""); //progname w/out X's
                    while (sic1.hasNextLine()) {
                        line = sic1.nextLine();
                        if (line.charAt(0) == 'E')
                            break;
                        if (line.charAt(0) == 'T')
                        {
                            startT = line.substring(3, 7); //STARTT + MAP[TEMP]
                            //startT2-->new start of T after relocation (startingOfT + program starting loc after relocation)
                            startT2 = Integer.toHexString(Integer.parseInt(startT, 16) + Integer.parseInt(map.get(temp), 16));
                            line2 = line.substring(9); //all t record
                            addData(startT2, line2); //add t record to memory

                        }
                    }
                }
            }
            sic1.close();
//-============================================================================================================
            //print gui before modification
//        for (int k = 0; k <= stop; k++) {
//            for (int j = 0; j < 17; j++) {
//                String text = mem[k][j];
//                if (k == 0 && j==0) {
//                    area1.setText(area1.getText() + "\t" + text);
//                }
//                else {
//                    area1.setText(area1.getText() + "\t\t" + text);
//                }
//            }
//            area1.setText(area1.getText() + "\n");
//        }
 //-============================================================================================================
 // M RECORD
            FileInputStream file2 = new FileInputStream("in.txt");
            Scanner sic2 = new Scanner(file2);
            f = 0;
            while (f < prog_names.size()) {

                line = sic2.nextLine();
                if (line.charAt(0) == 'H') {
                    f++;
                    temp = line.substring(1, 7);
                    temp = temp.replaceAll("X", ""); //progname w/out X's
                    while (sic2.hasNextLine()) {
                        line = sic2.nextLine();
                        if (line.charAt(0) == 'E') //khalast el program (ely warah program gedid)
                            break;
                        if (line.charAt(0) == 'M')
                        {
                            startT = line.substring(1, 7); //address of modication
                            //startM-->address of modication after relocation
                            startM = Integer.toHexString(Integer.parseInt(startT, 16) + Integer.parseInt(map.get(temp), 16));
                            lengthM = line.substring(7, 9); //05 or 06

                            op = line.substring(9, 10); // + or -
                            symbol = line.substring(10); //defintion
                            mrecord(startM, lengthM, op, symbol); //handle m record
                            //System.out.println(startM+" "+lengthM+" "+op+" "+symbol);
                        }
                    }


                }
            }
            sic1.close();
//-============================================================================================================
            //print gui
            for (int k = 0; k <= stop; k++) {
                for (int j = 0; j < 17; j++) {
                    String text = mem[k][j];
                    if (k == 0 && j == 0) {
                        area1.setText(area1.getText() + "\t" + text);
                    } else {
                        area1.setText(area1.getText() + "\t\t" + text);
                    }
                }
                area1.setText(area1.getText() + "\n");
            }
 //-============================================================================================================
        }
    }
 //-============================================================================================================
  //-============================================================================================================
 //-============================================================================================================
 //-============================================================================================================
    //FUNCTIONS
    //HANDLE T RECORD
        public void addData (String startT, String line2){
            int z = 0;
            stemp.append(startT); //stringbuilder gih start of T
            x = String.valueOf(stemp.charAt(stemp.length() - 1)); //stemp=4027 --> x=7
            stemp.setCharAt(stemp.length() - 1, '0'); //stemp=4020 (3ashan azbt makan fl coloumn address sah)
            for (int i = 0; i < line2.length() - 1; i += 2) { //3ashan n2sm el t record lkol 2 f list
                trecord.add(line2.substring(i, i + 2));
            }
            for (int i = 1; i <= stop; i++)  //3ashan n2of makan el start t3 el T--> a row w b col (index)
            {
                if ((Integer.parseInt(mem[i][0], 16)) == (Integer.parseInt(String.valueOf(stemp), 16)))
                {
                    a = i;
                    for (int j = 1; j < 17; j++) {
                        if ((Integer.parseInt(mem[0][j], 16) == Integer.parseInt(x, 16))) {
                            b = j;
                            break;
                        }
                    }
                    break;
                }
            }
            for (int i = a; i <= stop; i++) //3ashan n7ot el t record fl mem
            {
                if (i != 0) //3ashan mn7otsh f col el address
                {
                    for (int j = 1; j < 17; j++) {
                        if (i == a && j == 1) { //3ashan ba3d keda a2dr abd2 mn tany col(0 msh address)
                            j = b;
                        }
                        if (z < trecord.size()) { //counter l size el t record
                            mem[i][j] = trecord.get(z);
                            z++;
                        }
                    }
                }
            }
            stemp.delete(0, stemp.length()); //3ashan el t record ely waraha tb2a fadya
            trecord.clear();//3ashan el t record ely waraha tb2a fadya
        }
 //-============================================================================================================
 //-============================================================================================================
    //HANDLE M RECORD
        public void mrecord (String startM, String lengthM, String op, String symbol){

            stemp.append(startM); //START OF M RECORD
            StringBuilder temp = new StringBuilder();
            StringBuilder temp2 = new StringBuilder();
            StringBuilder temp3 = new StringBuilder();
            x = String.valueOf(stemp.charAt(stemp.length() - 1)); //stemp=4027 --> x=7
            stemp.setCharAt(stemp.length() - 1, '0'); ////stemp=4020 (3ashan azbt makan fl coloumn address sah)

           for (int i = 1; i <= stop; i++) //3ashan n2of makan el start t3 el M --> a row w b col
            {
                if ((Integer.parseInt(mem[i][0], 16)) == (Integer.parseInt(String.valueOf(stemp), 16))) {
                    a = i; //row
                    for (int j = 1; j < 17; j++) {
                        if ((Integer.parseInt(mem[0][j], 16) == Integer.parseInt(x, 16))) {
                            b = j; //col
                            break;
                        }
                    }
                    break;
                }
            }
            int z = 0; //counter 3ashan akhod 6 digits bs
            for (int i = a; i <= stop; i++)  //bageb el data ely m7taga a3dlha fl m record (6 digits)
            {
                if (i != 0) {
                    for (int j = 1; j < 17; j++) {

                        if (z < 3) {
                            if (i == a && j == 1) {
                                j = b;
                            }
                            temp.append(mem[i][j]);
                            z++;
                        }
                    }
                }
            }
            //temp--> fih 06 ely mmkn yb2o 3ayzen yt3dlo w law 5 7ayb2a fiha el zeyada
//----------------------------------------------------------------------------------------------------------
            temp2.append(temp); //naf temp bs 3ashan a3dl 3aleha law 7akhod 05 tb2a el 6th saved fl temp

                temp2.deleteCharAt(0); //law 05 7anshtghal 3ala akher 5 law 06 7anshtghal 3ala kolo 3ady

            temp6 = map.get(symbol); //gebna value el definition

            if (Objects.equals(op, "+"))  //7an7sb el data 7asb el modification -->temp 7
            {
                temp7 = Integer.toHexString(Integer.parseInt(temp2.toString(), 16) + Integer.parseInt(temp6, 16));

            } else if (Objects.equals(op, "-")) {
                temp7 = Integer.toHexString(Integer.parseInt(temp2.toString(), 16) - Integer.parseInt(temp6, 16));
            }

            //khly temp7 6 digits aw 5 digits 3ala 7asb gowa temp8
           if (temp7.length() > 6) {
                temp3.append(temp7);
                int u = 0;
                while (temp3.length() > 6) {
                    temp3.deleteCharAt(0);
                }
                temp7 = temp3.toString();
                temp8 = temp7;
            } else if (Objects.equals(lengthM, "05")) { //law 05 --> bakhly length temp7 5
                if (temp7.length() == 5)
                    temp8 = temp7;
                else if (temp7.length() == 4)
                    temp8 = "0" + temp7;
                else if (temp7.length() == 3)
                    temp8 = "00" + temp7;
                else if (temp7.length() == 2)
                    temp8 = "000" + temp7;
                else if (temp7.length() == 1)
                    temp8 = "0000" + temp7;
            } else if (Objects.equals(lengthM, "06")) { //law 06 --> bakhly length temp7 6
                if (temp7.length() == 6)
                    temp8 = temp7;
                else if (temp7.length() == 5)
                    temp8 = "0" + temp7;
                else if (temp7.length() == 4)
                    temp8 = "00" + temp7;
                else if (temp7.length() == 3)
                    temp8 = "000" + temp7;
                else if (temp7.length() == 2)
                    temp8 = "0000" + temp7;
                else if (temp7.length() == 1)
                    temp8 = "00000" + temp7;

            }
    //temp 8 fiha el data after modification (5 or 6 digits)

    //ba7ot el data tany kolaha gowa temp
            //law 05 bnsave awel digit f temp
            //law 06 bn7ot temp8 gowa temp
            if (Objects.equals(lengthM, "05")) {
                for (int i = 1; i < temp.length(); i++) {

                    temp.setCharAt(i, temp8.charAt(i - 1));
                }
            } else {
                for (int i = 0; i < temp.length(); i++) {
                    temp.setCharAt(i, temp8.charAt(i));
                }
            }
//temp fiha el data to be added after modification
//------------------------------------------------------------------------------------------------
            //add modified data fl memory tany
            ArrayList<String> mrec = new ArrayList<>();
            for (int i = 0; i < temp.length() - 1; i += 2) { //3ashan n2sm el temp l kol 2
                mrec.add(temp.substring(i, i + 2));
            }
            z = 0;
            for (int i = a; i <= stop; i++) { //ma3ana el a w el b mn fo2 gahzen
                if (i != 0) {
                    for (int j = 1; j < 17; j++) {

                        if (i == a && j == 1) {
                            j = b;
                        }
                        if (z < mrec.size()) { //counter l size el M record
                            mem[i][j] = mrec.get(z);
                            z++;
                        }
                    }
                }
            }
//---------------------------------------------------------------------------------------
            //delete kol haga 3ashan el M record ely warah byt2srsh
            stemp.delete(0, stemp.length());
            temp.delete(0, temp.length());
            temp2.delete(0, temp2.length());
            temp8 = "";
            temp7 = "";
            mrec.clear();
        }
//---------------------------------------------------------------------------------------
}
