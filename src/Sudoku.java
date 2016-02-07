/**
 * Copyright (C) 2015  Hongzhou Liu

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

import java.util.Scanner;
import java.util.ArrayList;

public class Sudoku{

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); //asks for initial values
        //int[][] grid = new int[9][9]; //game grid that displays current values
        int[][] lastGrid = new int[9][9];
        int[][] grid = new int[][]{
                {0,0,0,0,0,0,0,9,0},
                {0,0,0,9,0,0,3,6,1},
                {0,0,0,5,0,0,7,0,0},
                {0,2,1,0,0,3,0,0,4},
                {9,6,0,0,7,0,0,5,3},
                {3,0,0,4,0,0,9,2,0},
                {0,0,2,0,0,9,0,0,0},
                {8,3,7,0,0,1,0,0,0},
                {0,9,0,0,0,0,0,0,0}
        };
        ArrayList[][] possible = new ArrayList[9][9]; //keeps track of possible values for each grid square

        //Subgrids represent the 9 3x3 grids on a sudoku board in which no 2 numbers can be equal
        //  1|2|3
        //  4|5|6
        //  7|8|9
        int[][] subGrid1 = new int[3][3]; int[][] subGrid2 = new int[3][3]; int[][] subGrid3 = new int[3][3];
        int[][] subGrid4 = new int[3][3]; int[][] subGrid5 = new int[3][3]; int[][] subGrid6 = new int[3][3];
        int[][] subGrid7 = new int[3][3]; int[][] subGrid8 = new int[3][3]; int[][] subGrid9 = new int[3][3];
        //ArrayList[][] isNot = new ArrayList[9][9];
        int counter = 1;

        /*for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int num = populate(counter, input);
                grid[i][j] = num;
                //grid[i][j] = j + 1;
                counter++;
            }
        }*/


        print(grid);

        int x = 1;

        while(x == 1){
            populatePossible(grid, possible); //ADD FILTER
            populateSubGrids(subGrid1, subGrid2, subGrid3, subGrid4, subGrid5, subGrid6, subGrid7, subGrid8, subGrid9, grid);
            copyGrid(grid, lastGrid);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {

                    if(grid[i][j] == 0) {
                        checkHoriz(grid, possible, i, j); //TESTING
                        checkVert(grid, possible, i, j); //TESTING
                        checkSub(grid, subGrid1, subGrid2, subGrid3, subGrid4, subGrid5, subGrid6, subGrid7, subGrid8, subGrid9, i, j, possible);
                    }
                }
            }
            updateGrid(grid, possible);
            //copyGrid(grid, lastGrid);
            boolean isSame = checkForDifference(grid, lastGrid);
            if(isSame){
                System.out.println("Oops, I can only solve this far for this puzzle!");
                System.out.println();
                print(grid);
                break;
            }
            System.out.println();
            System.out.println();
            print(grid);
            System.out.println();
            System.out.println("Continue? Y or y for yes, N or n for no");
            if(input.nextLine().equalsIgnoreCase("n")) x = 2;
        }
    }

    //queries the user for the initial game grid and populates the grid with user input
    public static int populate(int counter, Scanner input) {
        System.out.printf("Enter number for square %d ", counter);
        System.out.println();
        System.out.println("If the square is blank, enter 0");
        int choice = input.nextInt();
        return choice;
    }

    //mirrors the main game grid onto the subgrids
    public static void populateSubGrids(int[][] subGrid1, int[][] subGrid2, int[][] subGrid3, int[][] subGrid4,
                                        int[][] subGrid5, int[][] subGrid6, int[][] subGrid7, int[][] subGrid8,
                                        int[][] subGrid9, int[][] grid){
        for(int i = 0; i <= 2; i++){
            for(int j = 0; j <= 2; j++){
                int transfer = grid[i][j];
                subGrid1[i][j] = transfer;
            }
        }
        for(int i = 0; i <= 2; i++){
            for(int j = 3; j <= 5; j++){
                int transfer = grid[i][j];
                subGrid2[i][j-3] = transfer;
            }
        }
        for(int i = 0; i <= 2; i++){
            for(int j = 6; j <= 8; j++){
                int transfer = grid[i][j];
                subGrid3[i][j-6] = transfer;
            }
        }
        for(int i = 3; i <= 5; i++){
            for(int j = 0; j <= 2; j++){
                int transfer = grid[i][j];
                subGrid4[i-3][j] = transfer;
            }
        }
        for(int i = 3; i <= 5; i++){
            for(int j = 3; j <= 5; j++){
                int transfer = grid[i][j];
                subGrid5[i-3][j-3] = transfer;
            }
        }
        for(int i = 3; i <= 5; i++){
            for(int j = 6; j <= 8; j++){
                int transfer = grid[i][j];
                subGrid6[i-3][j-6] = transfer;
            }
        }
        for(int i = 6; i <= 8; i++){
            for(int j = 0; j <= 2; j++){
                int transfer = grid[i][j];
                subGrid7[i-6][j] = transfer;
            }
        }
        for(int i = 6; i <= 8; i++){
            for(int j = 3; j <= 5; j++){
                int transfer = grid[i][j];
                subGrid8[i-6][j-3] = transfer;
            }
        }
        for(int i = 6; i <= 8; i++){
            for(int j = 6; j <= 8; j++){
                int transfer = grid[i][j];
                subGrid9[i-6][j-6] = transfer;
            }
        }

    }

    //prints out the subgrids, not used in final
    public static void printSubGrids(int[][] subGrid1, int[][] subGrid2, int[][] subGrid3, int[][] subGrid4,
                                     int[][] subGrid5, int[][] subGrid6, int[][] subGrid7, int[][] subGrid8,
                                     int[][] subGrid9){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid1[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                int num = subGrid2[a][b];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid3[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid4[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid5[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid6[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid7[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid8[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = subGrid9[i][j];
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    //prints out a copy of the current game grid
    public static void print(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int num = grid[i][j];

                    System.out.print(num + " ");


            }
            System.out.println();
        }
    }

    //runs at start, populates the possible array with 81 arraylists that track all possible ints for each blank entry
    //initially populates each arraylist with all ints 1-9
    //a list of possible answers is only created for empty/unknown (i,j) entries
    public static void populatePossible(int[][] grid, ArrayList[][] possible) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boolean isBlank = isBlank(grid, i, j);
                if(isBlank) {
                    possible[i][j] = new ArrayList<Integer>();
                    //isNot[i][j] = new ArrayList<Integer>();
                    //isNot[i][j].add(grid[i][j]);
                    for (int k = 1; k <= 9; k++) {
                        possible[i][j].add(k);
                    }
                }
            }
        }
    }

    //checks each unknown (i,j) entry to determine if it is blank or has an answer
    public static boolean isBlank(int[][] grid, int i, int j){
        if(grid[i][j] == 0){
            return true;
        }
        else{
            return false;
        }
    }

    //eliminates starting values from the possible arraylists. Early proof of concept, not used in final
    public static void eliminateInitial(int[][] grid, ArrayList[][] possible) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int init = grid[i][j];
                if(possible[i][j].contains(init)){
                    //System.out.println(init + "AAAA");
                    int ind = possible[i][j].indexOf(init);
                    possible[i][j].remove(ind);
                }
            }
        }
    }

    //checks all entries above and below the unknown (i,j) entry to eliminate non-possible choices for (i,j)
    public static void checkVert(int[][] grid, ArrayList[][] possible, int i, int j){
        //int iTest = 0;
        for(int t = 1; t < 9; t++){
            int iTest = i + t;
            if(iTest > 8) {
                iTest = (iTest - 8) - 1;
            }
            int examine = grid[iTest][j];
            if(grid[i][j] == 0) {
                if (possible[i][j].contains(examine)) {
                    int ind = possible[i][j].indexOf(examine);
                    possible[i][j].remove(ind);
                }
            }
        }

    }

    //checks all entries to the sides of the unknown (i,j) entry to eliminate non-possible choices for (i,j)
    public static void checkHoriz(int[][] grid, ArrayList[][] possible, int i, int j){
        for(int t = 1; t < 9; t++){
            int jTest = j + t;
            if(jTest > 8) {
                jTest = (jTest - 8) - 1;
            }
            int examine = grid[i][jTest];
            if(grid[i][j] == 0) {
                if (possible[i][j].contains(examine)) {
                    int ind = possible[i][j].indexOf(examine);
                    possible[i][j].remove(ind);
                }
            }
        }
    }

    //checks all entries within the subgrid occupied by the unknown (i,j) entry to eliminate non-possible choices for (i,j)
    public static void checkSub(int[][] grid, int[][] subGrid1, int[][] subGrid2, int[][] subGrid3, int[][] subGrid4,
                                int[][] subGrid5, int[][] subGrid6, int[][] subGrid7, int[][] subGrid8,
                                int[][] subGrid9, int i, int j, ArrayList[][] possible){

            if((i >= 0) && (i <= 2)){
                if((j >= 0) && (j <= 2)){
                    //subGrid 1
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid1[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
                if((j >= 3) && (j <= 5)){
                    //subGrid 2
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid2[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
                if((j >= 6) && (j <= 8)){
                    //subGrid 3
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid3[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
            }
            if((i >= 3) && (i <= 5)){
                if((j >= 0) && (j <= 2)){
                    //subGrid 4
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid4[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
                if((j >= 3) && (j <= 5)){
                    //subGrid 5
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid5[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
                if((j >= 6) && (j <= 8)){
                    //subGrid 6
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid6[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
            }
            if((i >= 6) && (i <= 8)){
                if((j >= 0) && (j <= 2)){
                    //subGrid 7
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid7[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
                if((j >= 3) && (j <= 5)){
                    //subGrid 8
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid8[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
                if((j >= 6) && (j <= 8)){
                    //subGrid 9
                    for(int m = 0; m < 3; m++){
                        for(int p = 0; p < 3; p++){
                            int test = subGrid9[m][p];
                            if (possible[i][j].contains(test)) {
                                int ind = possible[i][j].indexOf(test);
                                possible[i][j].remove(ind);
                            }
                        }
                    }
                }
            }
    }

    //updates the game grid by checking the list of possible choices for each unknown (i,j) entry. When there is only
    //one possible entry left the grid is updated to reflect the found answer
    public static void updateGrid(int[][] grid, ArrayList[][] possible){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(grid[i][j] == 0){
                  if(possible[i][j].size() == 1){
                      int replace = (Integer)possible[i][j].get(0);
                      grid[i][j] = replace;
                  }
                }
            }
        }
    }

    //copies the game grid after it has been updated to the lastGrid array. Grid and lastGrid are compared
    //to determine if the puzzle can be solved further. When grid is the same as lastGrid, no more iterations are possible
    public static void copyGrid(int[][] grid, int[][] lastGrid){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                lastGrid[i][j] = grid[i][j];
            }
        }
    }

    //compares grid and lastGrid, returns true if current grid is same as last iteration of grid
    public static boolean checkForDifference(int[][] grid, int[][] lastGrid){
        boolean isSame = true;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(grid[i][j] != lastGrid[i][j]) isSame = false;
            }
        }
        return isSame;
    }
}

