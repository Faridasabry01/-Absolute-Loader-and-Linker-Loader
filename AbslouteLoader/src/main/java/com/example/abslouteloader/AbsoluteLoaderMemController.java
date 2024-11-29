package com.example.abslouteloader;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;


public class AbsoluteLoaderMemController {
    @FXML
    public TextArea area1 = new TextArea();
    public Button ok;

    String[][] mem = new String[2050][17];
    ArrayList<String> trecord = new ArrayList<>();
    int a, b;
    int n = 20, noofrows, temp, temp3, stop;
    String start, length, rows, temp2, startT, line2, x;
    StringBuilder stemp = new StringBuilder();

    StringBuilder end = new StringBuilder();


    public void showdata() throws FileNotFoundException {

        FileInputStream file = new FileInputStream("in.txt");
        Scanner sic = new Scanner(file);
        String line;
        while (sic.hasNextLine()) {
            line = sic.nextLine();
            if (line.charAt(0) == 'H') {
                start = line.substring(7, 13);
                length = line.substring(13, 19); //end msh included
                end.append(Integer.toHexString(Integer.parseInt(start, 16) + Integer.parseInt(length, 16)));
                break;
            }
        }

        end.setCharAt(end.length() - 1, '0');
        noofrows = Integer.parseInt(String.valueOf(end));
        rows = Integer.toString(noofrows);
        noofrows = Math.ceilDivExact(Integer.parseInt(rows, 16), 16) + 1;
        n = noofrows; //msh accurate number of rows just to set up the memory

//-------------------------------------------------------------------------------------------------
        //set mem X
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 17; j++) {
                mem[i][j] = "x";
            }
        }
        //set row 0
        for (int i = 0; i < 17; i++) {
            if (i == 0)
                mem[0][i] = "Address";
            else {
                if (i > 10)
                    mem[0][i] = Integer.toHexString(Integer.valueOf(i - 1));
                else
                    mem[0][i] = Integer.valueOf(i - 1).toString();
            }
        }

        //-------------------------------------------------------------------------------------------------
        //set col 0
        temp = Integer.parseInt(start, 16); //start int
        temp3 = Integer.parseInt(String.valueOf(end), 16); //END
        for (int i = 0; i <= noofrows; i++) {
            temp2 = Integer.toHexString(temp + 16 * i); //(+16 3ashan el row fi 16 col mn 0->f)(* i 3ashan number of rows)
            if (Integer.parseInt(temp2, 16) > temp3)  //compare end decimal w/ current row value
            {
                stop = i;
                break;
            } else {
                if (temp2.length() == 2)
                    mem[i + 1][0] = "00" + temp2;
                else if (temp2.length() == 3)
                    mem[i + 1][0] = "0" + temp2;
                else if (temp2.length() == 1)
                    mem[i + 1][0] = "000" + temp2;
                else
                    mem[i + 1][0] = temp2;
            }

        }
//-------------------------------------------------------------------------------------------------
        //t record
        while (sic.hasNextLine()) {
            line = sic.nextLine();
            //System.out.println(line);
            if (line.charAt(0) == 'T') {
                startT = line.substring(3, 7);
                line2 = line.substring(9);
                addData(startT, line2);

            } else if (line.charAt(0) == 'E') {
                break;
            }

        }


//-------------------------------------------------------------------------------------------------
        //print gui
        for (int i = 0; i <= stop; i++) {
            for (int j = 0; j < 17; j++) {
                String text = mem[i][j];
                if (i == 0 && j==0) {
                    area1.setText(area1.getText() + "\t" + text);
                }
                else {

                    area1.setText(area1.getText() + "\t\t" + text);
                }

            }
            area1.setText(area1.getText() + "\n");
        }
//-------------------------------------------------------------------------------------------------

    }

    public void addData(String startT, String line2) {
        int z = 0;
        stemp.append(startT);
        x = String.valueOf(stemp.charAt(stemp.length() - 1));
        //System.out.println(x);
        stemp.setCharAt(stemp.length() - 1, '0');
        for (int i = 0; i < line2.length(); i += 2) {
            trecord.add(line2.substring(i, i + 2));
        }

        for (int i = 1; i <= stop; i++) {
//
            if ((Integer.parseInt(mem[i][0], 16)) == (Integer.parseInt(String.valueOf(stemp), 16))) {
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

        for (int i = a; i <= stop; i++) {
            if (i != 0) {
                for (int j = 1; j < 17; j++) {
                    if (i == a && j==1) {
                        j = b;
                    }
                    if (z < trecord.size()) {
                            mem[i][j] = trecord.get(z);
                            z++;
                    }
                }
            }


        }stemp.delete(0, stemp.length());
        trecord.clear();
    }
}
