import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class MusicPlayer
{
    static class Node {
        String song;
        Node next, prev;

        Node(String song) {
            this.song = song;
            this.next = null;
            this.prev = null;
        }
    }
    

    static Clip clip;
    static Node first = null;
    static Node current = null;
    static String extension = ".wav";
    static boolean play = false;
    static String app_name="spotify";

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/musicplayer";
        String user = "root";
        String pass = "";  // Make sure this is correct for your database
        String driver = "com.mysql.cj.jdbc.Driver";
        Connection con = DriverManager.getConnection(url, user, pass);
                if(con!=null){
                    System.out.println("connection is successful");
                }else{
                    System.out.println("not connected");
                }
/*
 * PORTION OF JAVA SELECT WICH TYPE OF SONG SELECT 
 */
        Scanner sc = new Scanner(System.in);
        MusicPlayer mp = new MusicPlayer();
        System.out.println("---------welcome to spotify---------");
        System.out.println("\t\t\t\t\tLOADING...");
        Thread.sleep(2000);
        System.out.print("\033[H\033[2J");
        System.out.println("which type song you want to play:\n1.Gujrati \n2.Hindi \n3.English");
        String type_song = sc.nextLine();
        String Show_query = "";
      Statement st=con.createStatement();
    /*  String sql="Insert into gujrati(songname,songapp) values('Dwarka Na Dev','spotify')";
        int i5=st.executeUpdate(sql);
        String sql2="Insert into hindi(songname,songapp) values('malangsajana','spotify'),('Deva Deva','spotify'),('Heeriye','spotify'),('Husan','spotify')";
        int i2=st.executeUpdate(sql2);
        String sql3="Insert into english(songname,songapp) values('brokenangal','spotify'),('Let Me Down Slowly','spotify')";
        int i3=st.executeUpdate(sql3);*/
//    System.out.println((i>0)?"Inserted":"failde"); */

         //   String tablename="";
        switch (type_song) {//parmnet song database mathi linklist ma song nakhva
            case "1":
                Show_query = "SELECT songname FROM gujrati";
                 
                break;
            case "2":
                Show_query = "SELECT songname FROM hindi";
              
                break;
            case "3":
                Show_query = "SELECT songname FROM english";

                break;

            default:
                System.out.println("enter valid input");
                
            break;
        }
        if (!Show_query.isEmpty()) {
            Statement st2 = con.createStatement();
            ResultSet rs = st2.executeQuery(Show_query);
            while (rs.next()) {
                mp.addSong(rs.getString("songname") + extension);
            }
        }

        try{
        while (true) {
            System.out.println(
                    "1.view playlist \n2.play song \n3.add song \n4.stop song \n5.next song \n6.previous song \n7.shuffle \n8.resume\n9.exit player");
            System.out.println("enter num which task you perform:");
                int x = sc.nextInt();
                String ch= String.valueOf(x);
                sc.nextLine();
                String playlistsql="";
                switch (ch) {
                    case "1"://PLAYLIST SHOWING PROTION 
                        mp.viewPlayList();
                        break;
                    
                    case "2"://PLAY SONG 
                        System.out.println("Enter song name to play:");
                        String playSong = sc.nextLine();
                        String activePlaySong = playSong.concat(extension);
                        Node temp = first;
                        int flag = 0;
                        if (first == null) {
                            System.out.println("playlist is empty:");
                        } else {
                            do {
                                if (temp.song.equalsIgnoreCase(activePlaySong)) {//temp.song linklist 
                                    play = true;
                                    flag = 1;
                                    break;
                                } else {
                                    temp = temp.next;
                                }
                            } while (temp != first);
                            if (flag == 1) {
                                current = temp;
                                File f1 = new File(current.song);//currant.song=songname linklist mathi finde karelu 
                                System.out.println("___________________________________________");
                                System.out.println();
                                System.out.println("Playing: " + playSong);
                                System.out.println("___________________________________________");
                                System.out.println();
                                Thread.sleep(1000);
                                AudioInputStream audioInput = AudioSystem.getAudioInputStream(f1);
                                clip = AudioSystem.getClip();
                                clip.open(audioInput);
                                if (f1.exists()) {
                                    clip.start();
                                } else {
                                    System.out.println("song is not available");
                                }
                            } else {
                                System.out.println("Song not found in playlist.");
                            }
                        }
                        break;

                        case "3":                            //localsong database and linklist ma nakhava
                        System.out.println("Enter song name which you want to add:");
                    
                        String song_name = sc.nextLine();
                        if(type_song.equals("1"))//type mismeth // which tabale you select in uappar swith 
                        {
                          playlistsql = "Insert into gujrati (songname,songapp) values(?,?)";
                        }
                        else if(type_song.equals("2"))
                        {
                          playlistsql = "Insert into hindi (songname,songapp) values(?,?)";
                        }
                        else if(type_song.equals("3"))
                        {
                          playlistsql= "Insert into english (songname,songapp) values(?,?)";
                        }
                        else
                        {
                            System.out.println("Invalid song type.");
                            break;
                        }
                            PreparedStatement playpst = con.prepareStatement(playlistsql);
                            playpst.setString(1,song_name);//case 1 song add karvu hoy te 
                            playpst.setString(2,app_name);
                            
                            
                            String Song = song_name.concat(extension);
                            File f = new File(Song);
                            if (f.exists()) {
                                mp.addSong(Song);
                                System.out.println("song added");
                                System.out.println("");
                            } else {
                                System.out.println("Song is Not available");
                            }
    
                            int r = playpst.executeUpdate();
                            System.out.println((r>0)?"Inserted database":"failde");
    
                            break;
    
                    

                    case "4":
                        if (play) {
                            clip.stop();
                            play = false;
                        } else {
                            System.out.println("nothing to stop");
                        }
                        break;

                    case "5"://NEXT SONG PLAY
                        if (play) {
                            clip.stop();
                            current = current.next;
                            File f2 = new File(current.song);
                            Thread.sleep(1000);
                            System.out.println("----------------------------------------");
                            System.out.println("Next playing is :"+ current.song);
                            System.out.println("----------------------------------------");

                            AudioInputStream audioStream = AudioSystem.getAudioInputStream(f2);
                            clip = AudioSystem.getClip();
                            clip.open(audioStream);
                            clip.start();
                        } else {
                            System.out.println("No song is currently playing.");
                        }
                        break;

                    case "6"://PRIVUES SONG PLAY
                        if (play) {
                            clip.stop();
                            current = current.prev;
                            File f3 = new File(current.song);
                            Thread.sleep(1000);
                            System.out.println("----------------------------------------------");
                            System.out.println("Previous song is :"+current.song);
                            System.out.println("----------------------------------------------");
                            AudioInputStream audioStream = AudioSystem.getAudioInputStream(f3);
                            clip = AudioSystem.getClip();
                            clip.open(audioStream);
                            clip.start();
                        } else {
                            System.out.println("No song is currently playing.");
                        }
                        break;

                        case "7"://SHUFFLE PLAYLIST
                        if (play) {
                            clip.stop();
                            int count = 0;
                            Node shuffle = first;
                            do {
                                count++;
                                shuffle = shuffle.next;
                            } while (shuffle != first);
                            int randomIndex = (int) (Math.random() * count) + 1;
                            Node shuffleNode = first;
                            for (int i = 1; i < randomIndex; i++) {
                                shuffleNode = shuffleNode.next;
                            }
                            File f2 = new File(shuffleNode.song);
                            Thread.sleep(1000);
                            System.out.println("----------------------------------------");
                            System.out.println("Shuffle playing is :" + shuffleNode.song);
                            System.out.println("----------------------------------------");
                            
              
                            AudioInputStream audioStream = AudioSystem.getAudioInputStream(f2);
                            clip = AudioSystem.getClip();
                            clip.open(audioStream);
                            clip.start();
                            current = shuffleNode;
                        } else {
                            System.out.println("No song is currently playing.");
                        }     
                        break;

                        case "8": // RESUME SONG
                         if (!play) {
                            clip.start();
                            play = true;
                              System.out.println("Song resumed.");
                             } else {
                                 System.out.println("Song is already playing.");
                            }
                        break;

                    case "9"://EXIT
                        System.exit(0);
                        break;

                       

                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        }catch(Exception e){
            System.out.println("enter vaild option:"+e.getMessage());
        }
    }
    static void addSong(String song) {
        Node n = new Node(song);
        if (first == null) {
            first = n;
            first.next = first;
            first.prev = first;
            current = first;
        } else {
            Node temp = first;
            while (temp.next != first) {
                temp = temp.next;
            }
            n.next = first;
            n.prev = temp;
            temp.next = n;
            first.prev = n;
        }
    }

    static void viewPlayList() {
        if (first == null) {
            System.out.println("empty");
        } else {
            Node temp = first;
            System.out.println("PLAYLIST.!!\n");
            int i = 1;
            System.out.println("----------Your PlayList Is------------");
            do {
                System.out.println(i + " : " + temp.song);
                i++;
                temp = temp.next;
            } while (temp != first);
            System.out.println("---------------------------------------");
                }
        System.out.println("");
    }
}
