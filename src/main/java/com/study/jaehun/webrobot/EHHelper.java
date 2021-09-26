package com.study.jaehun.webrobot;

public class EHHelper {
    static public String EmitTagAndSpacialCh(String str){
        str = EHHelper.RemoveTag(str);
        str = EHHelper.RemoveHtmlSpecialCh(str);
        str = EHHelper.RemoveSymbol(str);
        return str;
    }

    static public String RemoveTag(String src){
        try{
            while (true){
                int[] num = EHHelper.FindTag(src);
                if (num[0] < num[1]){
                    src = src.substring(0,num[0]) + src.substring(num[1]+1);
                }else {
                    src = src.substring(0, num[1]) + src.substring(num[1]+1);
                }
            }
        }catch (Exception e){
            return src;
        }
    }

    static public int[] FindTag(String str){
        int[] num = {0,0};
        num[0] = str.indexOf("<");
        num[1] = str.indexOf(">");
        return num;
    }

    static public String RemoveSymbol(String src){
        StringBuilder dest = new StringBuilder();
        String[] splitSrc = src.split("");
        for ( String elem : splitSrc) {
            boolean isNumeric = elem.chars().allMatch( Character::isDigit );
            if (!isNumeric || elem.matches(" ")){
                dest.append(elem);
            } else{
                dest.append(" ");
            }
        }
        return dest.toString();
    }

    static public String RemoveHtmlSpecialCh(String src){
        try{
            while (true){
                int[] num = EHHelper.FindHtmlSpecialCh(src);
                if (num[0] < num[1]){
                    src = src.substring(0,num[0]) + src.substring(num[1]+1);
                }else {
                    src = src.substring(0, num[1]) + src.substring(num[1]+1);
                }
            }
        }catch (Exception e){
            return src;
        }
    }

    static public int[] FindHtmlSpecialCh(String src){
        int[] num = {0,0};
        num[0] = src.indexOf("&");
        num[1] = src.indexOf(";");
        return num;
    }
}
