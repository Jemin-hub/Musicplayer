import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

interface IAudioPlayer {
    void addTrack(String track);
    void playTrack(String track);
    void pauseTrack();
    void resumeTrack();
    void previousTrack();
    void nextTrack();
    void removeTrack(String track);
    void displayTrackList();
}

 abstract class AudioPlayer implements IAudioPlayer {
    protected static List<String> trackList;
    protected String currentTrack;
    protected boolean isPlaying;
    protected Clip audioClip;

     AudioPlayer() {
        trackList = new ArrayList<>();
        currentTrack = null;
        isPlaying = false;
    }

     public void addTrack(String track) {
        trackList.add(track);
    }

     public void playTrack(String track) {
        if (trackList.contains(track)) {
            currentTrack = track;
            isPlaying = true;
            File audioFile = new File(currentTrack + ".wav");
            System.out.println("Trying to play: " + audioFile.getAbsolutePath()); // Print the file path
            if (audioFile.exists()) {
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    audioClip = AudioSystem.getClip();
                    audioClip.open(audioStream);
                    audioClip.start();
                    System.out.println("Playing: " + currentTrack);
                } catch (Exception e) {
                    System.out.println("Error playing track: " + e.getMessage());
                }
            } else {
                System.out.println("File not found: " + audioFile.getAbsolutePath());
            }
        } else {
            System.out.println("Track not found in playlist.");
        }
    }

     public void pauseTrack() {
        if (isPlaying) {
            audioClip.stop();
            isPlaying = false;
        } else {
            System.out.println("Nothing to pause.");
        }
    }

     public void resumeTrack() {
        if (!isPlaying) {
            audioClip.start();
            isPlaying = true;
        } else {
            System.out.println("Track is already playing.");
        }
    }

     public void previousTrack() {
        if (isPlaying) {
            audioClip.stop();
            int index = trackList.indexOf(currentTrack);
            if (index > 0) {
                currentTrack = trackList.get(index - 1);
                playTrack(currentTrack);
            } else {
                System.out.println("No previous track.");
            }
        } else {
            System.out.println("No track is currently playing.");
        }
    }

     public void nextTrack() {
        if (isPlaying) {
            audioClip.stop();
            int index = trackList.indexOf(currentTrack);
            if (index < trackList.size() - 1) {
                currentTrack = trackList.get(index + 1);
                playTrack(currentTrack);
            } else {
                System.out.println("No next track.");
            }
        } else {
            System.out.println("No track is currently playing.");
        }
    }

     public void removeTrack(String track) {
        if (trackList.contains(track)) {
            trackList.remove(track);
            System.out.println("Track removed from playlist.");
        } else {
            System.out.println("Track not found in playlist.");
        }
    }

   public  void displayTrackList() {
        System.out.println("Available Tracks:");
        for (int i = 0; i < trackList.size(); i++) {
            System.out.println((i + 1) + " : " + trackList.get(i));
        }
        System.out.println("");
    }
}

// Generalized Player for Database Interaction
class UniversalAudioPlayer extends AudioPlayer {
    private Connection con;

     UniversalAudioPlayer(Connection con) {
        this.con = con;
    }

   public  void addTrack(String track) {
        super.addTrack(track);
        String query = "INSERT INTO tracks (trackname, source) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, track);
            pst.setString(2, "Spotify");
            pst.executeUpdate();
            System.out.println("Track added to database.");
        } catch (Exception e) {
            System.out.println("Error adding track to database: " + e.getMessage());
        }
    }

   public   void playTrack(String track) {
        super.playTrack(track);
    }

   public  void removeTrack(String track) {
        super.removeTrack(track);
        String query = "DELETE FROM tracks WHERE trackname = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, track);
            pst.executeUpdate();
            System.out.println("Track removed from database.");
        } catch (Exception e) {
            System.out.println("Error removing track from database: " + e.getMessage());
        }
    }
}

// Main class for interaction
class MainPlayerApp {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicdb", "roof", "");

        CircularTrackList trackList = new CircularTrackList();
        IAudioPlayer player = new UniversalAudioPlayer(con);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Play Track \n2. Add Track \n3. Pause Track \n4. Resume Track \n5. Previous Track \n6. Next Track \n7. Remove Track \n8. View Track List (Sorted) \n9. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter track name: ");
                    String track = scanner.nextLine();
                    player.playTrack(track);
                    break;
                case 2:
                    System.out.print("Enter track name: ");
                    track = scanner.nextLine();
                    player.addTrack(track);
                    trackList.addNode(track);
                    break;
                case 3:
                    player.pauseTrack();
                    break;
                case 4:
                    player.resumeTrack();
                    break;
                case 5:
                    player.previousTrack();
                    break;
                case 6:
                    player.nextTrack();
                    break;
                case 7:
                    System.out.print("Enter track name: ");
                    track = scanner.nextLine();
                    player.removeTrack(track);
                    break;
                case 8:
                    trackList.sortList();
                    trackList.displayList();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

// Doubly Circular Linked List for managing tracks
class CircularTrackList {
    public Node head;
    public Node tail;
    public int size;

     CircularTrackList() {
        head = null;
        tail = null;
        size = 0;
    }

      class Node {
        String data;
        Node prev;
        Node next;

         Node(String data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

     void addNode(String track) {
        Node newNode = new Node(track);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

     void sortList() {
        Node current = head;
        String temp;

        while (current != null) {
            Node nextNode = current.next;
            while (nextNode != null) {
                if (current.data.compareTo(nextNode.data) > 0) {
                    temp = current.data;
                    current.data = nextNode.data;
                    nextNode.data = temp;
                }
                nextNode = nextNode.next;
            }
            current = current.next;
        }

        // Clear and reload the sorted list into the trackList
        AudioPlayer.trackList.clear();
        Node node = head;
        while (node != null) {
            AudioPlayer.trackList.add(node.data);
            node = node.next;
        }
    }

     void displayList() {
        Node current = head;
        System.out.println("-------------------");
        while (current != null) {
            System.out.println(current.data + " ");
            current = current.next;
        }
        System.out.println("-------------------");
        System.out.println();
    }
}