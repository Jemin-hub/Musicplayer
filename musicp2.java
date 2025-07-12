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

class MusicPlayer2 {
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


    // using circuler doubly link list
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
    // using circuler doubly link list
    static void removeSong(String songName) {
        if (first == null) {
            System.out.println("Playlist is empty.");
        } else {
            Node temp = first;
            do {
                if (temp.song.equalsIgnoreCase(songName + extension)) {
                    if (temp == first) {
                        if (temp.next == first) {
                            first = null;
                        } else {
                            first = temp.next;
                            temp.next.prev = first;
                        }
                    } else {
                        temp.prev.next = temp.next;
                        temp.next.prev = temp.prev;
                    }
                    System.out.println("Song removed from playlist.");
                    return;
                }
                temp = temp.next;
            } while (temp != first);
            System.out.println("Song not found in playlist.");
        }
    }
    
    // using circuler linked list
    static void display() {
        if (first == null) {
            System.out.println("empty");
        } else {
            Node temp = first;
            System.out.println("PLAYLIST.!!\n");
            int i = 1;
            do {
                System.out.println(i + " : " + temp.song);
                i++;
                temp = temp.next;
            } while (temp != first);
        }
        System.out.println("");
    }

    public static void main(String[] args) throws Exception {
/*
 *               JDBC PORTION 
 */
String url = "jdbc:mysql://localhost:3306/project";
String user = "root";
String pass = ""; 
String driver = "com.mysql.cj.jdbc.Driver";
Connection con = DriverManager.getConnection(url, user, pass);
        if(con!=null){
            System.out.println("connection successful.");
    Statement st = con.createStatement();
} else {
    System.out.println("connection Failed.");
}

  //PORTION OF JAVA SELECT WHICH TYPE OF SONG SELECT 
        Scanner sc = new Scanner(System.in);
        MusicPlayer2 mp = new MusicPlayer2();
        System.out.println("----------------------------------------");
        System.out.println("Select language for listening songs.");
        System.out.println("\n1.Gujrati \n2.Hindi \n3.English \n4.Punjabi");

        System.out.println("----------------------------------------");
        System.out.print("Enter number: ");
        String type_song = sc.nextLine(); 
        String Show_query = "";         //fetching variable
       
     
        switch (type_song) {                                      //permanent song database mathi linklist ma song nakhva
            case "1":
                Show_query = "SELECT songname FROM gujrati";
                 
                break;
            case "2":
                Show_query = "SELECT songname FROM hindi";
              
                break;
            case "3":
                Show_query = "SELECT songname FROM english";

                break;
            case "4":
                Show_query = "SELECT songname FROM punjabi";

                break;

            default:
                System.out.println("enter valid input");
                
            break;
        }
        if (!Show_query.isEmpty()) {
            Statement st2 = con.createStatement();
            ResultSet rs = st2.executeQuery(Show_query);
            while (rs.next()) {
                mp.addSong(rs.getString("songname") + extension);//all song je permenet data base mathi linklist ma nakhva 
            }
        }

        /*
         * SELECT WHICH TASK YOU PERFORM 
         */
        try{
        while (true) {
            System.out.println("-----------------------------------");
            System.out.println(
                    "1.view songs \n2.play song \n3.add song \n4.stop song \n5.resume song \n6.previous song \n7.next song \n8.Remove song \n9.exit");
            System.out.println("-----------------------------------");

            System.out.print("enter number:");
                int x = sc.nextInt();
                System.out.println("-----------------------------------");
                String ch= String.valueOf(x);
                sc.nextLine();
                String playlistsql="";
                switch (ch) {
                    case "1"://PLAYLIST SHOWING PROTION 
                    mp.display();
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
                                if (temp.song.equalsIgnoreCase(activePlaySong)) {         
                                    play = true;
                                    flag = 1;
                                    break;
                                } else {
                                    temp = temp.next;
                                }
                            } while (temp != first);
                            if (flag == 1) {
                                current = temp;
                                File f1 = new File(current.song);                       //current.song=songname linklist mathi find karelu 
                                System.out.println("Playing: " + playSong);
                                AudioInputStream audioInput = AudioSystem.getAudioInputStream(f1);
                                clip = AudioSystem.getClip();
                                clip.open(audioInput);
                                if (f1.exists()) {
                                    clip.start();
                                } else {
                                    System.out.println("song is added.");
                                }
                            } else {
                                System.out.println("Song not found in playlist.");
                            }
                        }
                        break;
                        
                        case "3":                            //localsong database and linklist ma nakhava
                        System.out.println("Enter song name which you want to add:");
                    
                        String song_name = sc.nextLine();
                        if(type_song.equals("1"))//type mismatched // which table you select in upper switch //song added in database
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
                        else if(type_song.equals("4"))
                        {
                          playlistsql= "Insert into punjabi (songname,songapp) values(?,?)";
                        }
                        else
                        {
                            System.out.println("Invalid song type.");
                            break;
                        }
                            PreparedStatement playpst = con.prepareStatement(playlistsql);
                            playpst.setString(1,song_name);              //case 1 song add karvu hoy te 
                            playpst.setString(2,app_name);
                            
                            
                            String Song = song_name.concat(extension);    //song add in linked list.
                            File f = new File(Song);
                            if (f.exists()) {
                                mp.addSong(Song);
                            }
                            int r = playpst.executeUpdate();
                            System.out.println((r>0)?"Insert Success":" Insert failed");
    
                            break;
    

                        case "4":       // stop the song
                        if (play) {
                            clip.stop();
                            play = false;
                        } else {
                            System.out.println("nothing to stop");
                        }
                        break;

                        case "5": // RESUME SONG
                         if (!play) {
                            clip.start();
                            play = true;
                              System.out.println("Song resumed.");
                             } else {
                                 System.out.println("Song is already playing.");
                            }
                        break;
                            
                        case "6": // PREVIOUS SONG
                                if (play) {
                                 clip.stop();
                                    current = current.prev;
                                 File f3 = new File(current.song);
                                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(f3);
                                 clip = AudioSystem.getClip();
                                 clip.open(audioStream);
                                 clip.start();
                                 System.out.println("Now playing: " + current.song); // Display the song name
                             } else {
                                 System.out.println("No song is currently playing.");
                             }
                             break;

                        case "7": // NEXT SONG
                             if (play) {
                                 clip.stop();
                                 current = current.next;
                                 File f2 = new File(current.song);
                                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(f2);
                                 clip = AudioSystem.getClip();
                                 clip.open(audioStream);
                                 clip.start();
                                 System.out.println("Now playing: " + current.song); // Display the song name
                             } else {
                                 System.out.println("No song is currently playing.");
                             }
                             break;
                    
                        case "8": // REMOVE SONG
                        System.out.println("Enter song name to remove:");
                        String removeSongName = sc.nextLine();
                        String removeQuery = "";
                        switch (type_song) {
                            case "1":
                                removeQuery = "DELETE FROM gujrati WHERE songname = ?";
                                break;
                            case "2":
                                removeQuery = "DELETE FROM hindi WHERE songname = ?";
                                break;
                            case "3":
                                removeQuery = "DELETE FROM english WHERE songname = ?";
                                break;
                            case "4":
                                removeQuery = "DELETE FROM punjabi WHERE songname = ?";
                                break;
                            default:
                                System.out.println("song type.");
                                break;
                        }
                        if (!removeQuery.isEmpty()) {
                            PreparedStatement removePst = con.prepareStatement(removeQuery);
                            removePst.setString(1, removeSongName);
                            int r2 = removePst.executeUpdate();
                            if (r2 > 0) {
                                System.out.println("Song removed from database.");
                                removeSong(removeSongName); // Call the removeSong method to remove from linked list
                            } else {
                                System.out.println("Song not found in database.");
                            }
                        }
                        break;
                       
                        case "9":      //EXIT
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
}