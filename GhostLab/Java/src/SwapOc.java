import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.regex.*;


public class SwapOc {


     public static void test(Player p){
       p.updateStart();
     }

     public String check_number(String s) throws NumberFormatException {
       int res = Integer.parseInt(s);
       if(res <10) return "0"+res;
       return res+"";
     }

     // pos : position du premier bit
     // inv : nombre de var a inversé
     // x la position dans le tableau splité
     public String littleToBig(byte[] tab, int pos, int inv, int x){

       String res = "";
       String mess = new String (tab);
       String[] sp_msg = mess.split(" ");
       for(int k = 0; k < inv; k++){
         res = "";
         byte[] b = new byte[2];
         b[0] = tab[pos+1];
         b[1] = tab[pos];
         String changed = new String(b);
         int num = (int)changed.charAt(0);     //System.out.println("Num : "+num);
         short c = (short)num;     // System.out.println("la post dans le tab splité X : "+x);
         String var = check_number(c+""); // pu d'inspiraiton pour nommer les vars :'(*/
         if(x == sp_msg.length-1)
           sp_msg[x] = var + "***";
         else
           sp_msg[x] = var + "";
         //System.out.println("SP MAX : "+sp_msg[x]);
         for(int i = 0; i<sp_msg.length; i++){
           if(i == sp_msg.length-1)
             res+= sp_msg[i];
           else
             res+= sp_msg[i]+" ";
         }
         pos = pos + 3;
         x++;
       }
       System.out.println("RES : "+res);
       return res;
     }

     // x : pos dans le split
     // pos : position du bit a inversé dans le tableau
     // tab : tableau de byte
     // inv : nb truc a inversé
     public  String reverseAfterRead(byte [] s){
       String str = new String(s);
       String[] tab2 = str.split(" ");
       String tmp = "";

       if(tab2.length == 1){
         // [IQUIT***], [GOBYE***], [GLIS?***], [START***]. [UNREG***] [DUNNO***] [MALL!***]. [SEND!***] [NSEND***] [REGNO***]
         //System.out.println("ICI : "+tab2[0]+" taille :  "+tab2[0].length());

         if(tab2[0].substring(0,6).equals("GOBYE***"))         tmp = "GOBYE***";
         else if(tab2[0].substring(0,7).equals("IQUIT***"))   tmp = "IQUIT***";
         else if(tab2[0].substring(0,7).equals("MALL!***"))   tmp = "MALL!***";
         else if(tab2[0].substring(0,8).equals("START***"))  tmp = "START***";
         else if(tab2[0].substring(0,8).equals("UNREG***"))  tmp = "UNREG***";
         else if(tab2[0].substring(0,8).equals("DUNNO***"))  tmp = "DUNNO***";
         else if(tab2[0].substring(0,8).equals("SEND!***"))  tmp = "SEND!***";
         else if(tab2[0].substring(0,8).equals("REGNO***"))  tmp = "REGNO***";
         else if(tab2[0].substring(0,9).equals("GLIS?***")) tmp =  "GLIS?***";
         else if(tab2[0].substring(0,9).equals("NSEND***")) tmp = "NSEND***";

       } else {
         /*
         GAMES, GAME, NEW, REG, REGOK, UNREGOK,  SIZE?, SIZE!,  LIST!, PLAYER, WELCO, POSIT, UPMOV,DOMOV,
         LEMOV, RIMOV, MOV, MOVEF,  GLIS!, GPLYR,  MALL!, SEND?,
         */
         String type = tab2[0];
         switch(type){
           case "GAMES" :   tmp = littleToBig(s,6,1,1); break; // [GAMES␣n***]
           case "GAME" :    tmp = littleToBig(s,5,2,1); break; // [GAME␣m␣s***]
           case "NEW" :     tmp = str; break; // [NEW␣id␣port***] // rien a inversé
           case "REG" :     tmp = littleToBig(s,3+tab2[1].length()+tab2[2].length()+2,1,3); break; // [REG␣id␣port␣m***]
           case "REGOK" :   tmp = littleToBig(s,6,1,1); break; // [REGOK␣m***]
           case "UNREGOK" : tmp = littleToBig(s,8,1,1); break; // [UNREGOK␣m***]
           case "SIZE?" :   tmp = littleToBig(s,6,1,1); break; // [SIZE?␣m***]
           case "SIZE!" :   tmp = littleToBig(s,6,3,1); break; // [SIZE!␣m␣h␣w***]
           case "LIST!" :   tmp = littleToBig(s,6,2,1); break; // [LIST!␣m␣s***]
           case "LIST?" :   tmp = littleToBig(s,6,1,1); break; // [LIST!␣m***]
           case "PLAYER" :  tmp = str; break; // [PLAYER␣id***]
           case "WELCO" : tmp = littleToBig(s,8,4,1); break; // [WELCO␣m␣h␣w␣f␣ip␣port***]
           case "POSIT" :     tmp = str; break; // [POSIT␣id␣x␣y***]
           case "UPMOV":       tmp = littleToBig(s,3,1,1); break; // [UPMOV␣d***]
           case "DOMOV":     tmp = littleToBig(s,5,1,1); break; // [DOMOV␣d***]
           case "LEMOV":     tmp = littleToBig(s,5,1,1); break;  // [LEMOV␣d***]
           case "RIMOV":    tmp = littleToBig(s,6,1,1); break; // [RIMOV␣d***]
           case "MOV":      tmp = str; break; // [MOVE!␣x␣y***]
           case "MOVEF":      tmp = str; break; //[MOVEF␣x␣y␣p***]
           case "GLIS!":   tmp = littleToBig(s,7,1,1); break; // [GLIS!␣s***]
           case "GPLYR":  tmp = str; break; // [GPLYR␣id␣x␣y␣p***]
           case "MALL?":     tmp = str; break; // [MALL?␣mess***]
           case "SEND?":    tmp = str; break; // [SEND?␣id␣mess***]
           default :  break;
         }
       }
       return tmp;

     }


    public String readInfo(InputStream input) throws Exception {
       byte[] b = new byte[256];
       int bytesRead = input.read(b);
       String res = reverseAfterRead(b);
       System.out.println("TCP Reçu   --- ["+res+"]");
       return res;
    }


     // inv : nombre de fois a inversé
     // pos : posiiton de la premiere var a inversé
     byte[] bigToLittle(String s, int pos,int inv){
        byte[] data = s.getBytes();
        for(int k = 0; k < inv; k++){
          int entier = Integer.parseInt(s.substring(pos,pos+2));
          short c = (short)entier;
          byte[] tab = new byte[2];
          tab[0] = (byte) (0xFF & (c >> 8 * 1));
          tab[1] = (byte) (0xFF & (c >> 8 * 0)); // ou bien tab[0] = c, vue que le max est 99 et qu'il depasse jamais 128
          data[pos] = tab[0];
          data[pos+1] = tab[1];
          pos = pos + 3;
        }
        return data;
     }

    /*public String ultraPurge(String s){
       String
     }*/

     public  byte [] reverseBeforeWrite(String s){
       String[] tab = s.split(" ");
       byte[] data  =  new byte[250];
       String tmp = "";


       if(tab.length == 1){
         // [IQUIT***], [GOBYE***], [GLIS?***], [START***]. [UNREG***] [DUNNO***] [MALL!***]. [SEND!***] [NSEND***] [REGNO***]

         if(tab[0].substring(0,6).equals("GOBYE***"))         tmp = "GOBYE***";
         else if(tab[0].substring(0,7).equals("IQUIT***"))   tmp = "IQUIT***";
         else if(tab[0].substring(0,7).equals("MALL!***"))   tmp = "MALL!***";
         else if(tab[0].substring(0,8).equals("START***"))  tmp = "START***";
         else if(tab[0].substring(0,8).equals("UNREG***"))  tmp = "UNREG***";
         else if(tab[0].substring(0,8).equals("DUNNO***"))  tmp = "DUNNO***";
         else if(tab[0].substring(0,8).equals("SEND!***"))  tmp = "SEND!***";
         else if(tab[0].substring(0,8).equals("REGNO***"))  tmp = "REGNO***";
         else if(tab[0].substring(0,9).equals("GLIS?***")) tmp = "GLIS?***";
         else if(tab[0].substring(0,9).equals("NSEND***")) tmp = "NSEND***";



         return tmp.getBytes();

       } else {
         /*
         GAMES, GAME, NEW, REG, REGOK, UNREGOK,  SIZE?, SIZE!,  LIST!, PLAYER, WELCO, POSIT, UPMOV,DOMOV,
         LEMOV, RIMOV, MOV, MOVEF,  GLIS!, GPLYR,  MALL!, SEND?,
         */
         String type = tab[0];
         switch(type){
           case "GAMES" :   data =  bigToLittle(s,6,1); break; // [GAMES␣n***]
           case "GAME" :    data = bigToLittle(s,5,2); break; // [GAME␣m␣s***]
           case "NEW" :     data = s.getBytes(); break; // [NEW␣id␣port***] // rien a inversé
           case "REG" :     data = bigToLittle(s,4+tab[1].length()+tab[2].length()+2,1); break; // [REG␣id␣port␣m***]
           case "REGOK" :   data = bigToLittle(s,6,1); break; // [REGOK␣m***]
           case "UNREGOK" : data = bigToLittle(s,8,1); break; // [UNREGOK␣m***]
           case "SIZE?" :   data = bigToLittle(s,6,1); break; // [SIZE?␣m***]
           case "SIZE!" :   data = bigToLittle(s,6,3); break; // [SIZE!␣m␣h␣w***]
           case "LIST!" :   data = bigToLittle(s,6,2); break; // [LIST!␣m␣s***]
           case "LIST?" :   data = bigToLittle(s,6,1); break; // [LIST!␣m***]
           case "PLAYER" :  data = s.getBytes(); break; // [PLAYER␣id***]
           case "WELCO" : data = bigToLittle(s,8,4); break; // [WELCO␣m␣h␣w␣f␣ip␣port***]
           case "POSIT" :     data = s.getBytes(); break; // [POSIT␣id␣x␣y***]
           case "UPMOV":       data = bigToLittle(s,3,1); break; // [UPMOV␣d***]
           case "DOMOV":     data = bigToLittle(s,5,1); break; // [DOMOV␣d***]
           case "LEMOV":     data = bigToLittle(s,5,1); break;  // [LEMOV␣d***]
           case "RIMOV":    data = bigToLittle(s,6,1); break; // [RIMOV␣d***]
           case "MOV":      data = s.getBytes(); break; // [MOVE!␣x␣y***]
           case "MOVEF":      data = s.getBytes(); break; //[MOVEF␣x␣y␣p***]
           case "GLIS!":   data = bigToLittle(s,7,1); break; // [GLIS!␣s***]
           case "GPLYR":  data = s.getBytes(); break; // [GPLYR␣id␣x␣y␣p***]
           case "MALL?":     data = s.getBytes(); break; // [MALL?␣mess***]
           case "SEND?":    data = s.getBytes(); break; // [SEND?␣id␣mess***]
           default : break;

         }
         return data;
       }
     }




     // output stream connecté a une socket, msg, pos : position du bit a inverser
     public void writeInfo(OutputStream output, String msg) throws Exception {
       System.out.println("TCP Envoyé --- ["+ msg +"]");
       byte[] tab = reverseBeforeWrite(msg);
       output.write(tab);
       output.flush();
     }

     public static void main(){
       
     }



}
