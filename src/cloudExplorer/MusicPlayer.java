package cloudExplorer;

import static cloudExplorer.NewJFrame.jTextArea1;
import jaco.mp3.player.MP3Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Native;
import java.net.URL;
import javafx.scene.media.MediaPlayer;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MusicPlayer implements Runnable {

    NewJFrame mainFrame;

    public MusicPlayer(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        try {

            URL music_url = null;

            final MP3Player mp3 = new MP3Player();

            final JButton stopMusic = new JButton("Stop Music");
            final JButton replayMusic = new JButton("Play/Replay");
            final JButton forwardMusic = new JButton("Forward");
            final JButton backwardMusic = new JButton("Backward");
            final JButton closeMusic = new JButton("Close");

            stopMusic.setBackground(Color.white);
            stopMusic.setForeground(Color.blue);
            stopMusic.setBorder(null);
            replayMusic.setBackground(Color.white);
            replayMusic.setBorder(null);
            replayMusic.setForeground(Color.blue);
            backwardMusic.setBackground(Color.white);
            backwardMusic.setBorder(null);
            backwardMusic.setForeground(Color.blue);
            forwardMusic.setBackground(Color.white);
            forwardMusic.setBorder(null);
            forwardMusic.setForeground(Color.blue);

            ImageIcon play = new ImageIcon(
                    getClass().getResource("play.png"));
            replayMusic.setIcon(play);

            ImageIcon stop = new ImageIcon(
                    getClass().getResource("abort.png"));
            stopMusic.setIcon(stop);

            ImageIcon forward = new ImageIcon(
                    getClass().getResource("forward.png"));
            forwardMusic.setIcon(forward);

            ImageIcon rewind = new ImageIcon(
                    getClass().getResource("rewind.png"));
            backwardMusic.setIcon(rewind);

            forwardMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mp3.skipForward();
                }
            });

            backwardMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mp3.skipBackward();
                }
            });

            stopMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mp3.stop();
                    mainFrame.jButton17.setEnabled(true);
                }
            });
            replayMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mp3.stop();
                    mp3.play();
                    mainFrame.jButton17.setEnabled(false);
                }
            });

            int count = 0;
            for (int h = 1; h != mainFrame.previous_objectarray_length; h++) {
                if (mainFrame.object_item[h].isSelected()) {
                    if (mainFrame.object_item[h].getText().contains(".mp3")) {
                        mainFrame.objectacl.setACLpublic(mainFrame.object_item[h].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                        String url = mainFrame.objectacl.setACLurl(mainFrame.object_item[h].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                        url = url.replace("Pre-Signed URL = ", "");
                        music_url = (new URL(url));
                        mp3.addToPlayList(music_url);
                        count++;
                    } else {
                        mainFrame.objectacl.setACLpublic(mainFrame.object_item[h].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                        String url = mainFrame.objectacl.setACLurl(mainFrame.object_item[h].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                        url = url.replace("Pre-Signed URL = ", "");
                        music_url = (new URL(url));
                        JFrame mediaTest = new JFrame("Movie Player");
                        MediaLocator mlr = new MediaLocator(music_url);
                        mediaTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        Player mediaPlayer = Manager.createRealizedPlayer(music_url);
                        Component video = mediaPlayer.getVisualComponent();
                        Component controls = mediaPlayer.getControlPanelComponent();
                        mediaPlayer.start();
                    }
                    if (count == 1) {
                        mainFrame.jPanel14.removeAll();
                        mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
                        mainFrame.jPanel14.add(replayMusic);
                        mainFrame.jPanel14.add(forwardMusic);
                        mainFrame.jPanel14.add(backwardMusic);
                        mainFrame.jPanel14.add(stopMusic);
                        mainFrame.jPanel14.repaint();
                        mainFrame.jPanel14.revalidate();
                        mainFrame.jPanel14.validate();
                        mainFrame.jButton17.setEnabled(false);
                        mp3.play();
                    }
                }
            }

        } catch (Exception mp3player) {
            jTextArea1.append("\n" + mp3player.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    void startc() {
        (new Thread(new MusicPlayer(mainFrame))).start();
    }
}
