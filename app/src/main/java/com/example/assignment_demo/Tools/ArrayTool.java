package com.example.assignment_demo.Tools;

public class ArrayTool {
    public static int findItemPosition(Object[] targetArray,Object searchItem){
        for(int i = 0; i < targetArray.length; i++){
            if(targetArray[i] == searchItem){
                return i;
            }
        }
        return -1;
    }
}
