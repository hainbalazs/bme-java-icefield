import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;


public class Controller extends JFrame implements ActionListener {

    CardLayout cl = new CardLayout();
    JPanel mainPanel = new JPanel();

    Controller(){
        super("IceField");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(cl);
        JPanel setup = new JPanel();
        JButton b1 = new JButton("3x3");
        b1.setName("9");
        JButton b2 = new JButton("4x4");
        b2.setName("16");
        b1.addActionListener(this);
        b2.addActionListener(this);
        setup.add(b1);
        setup.add(b2);
        getContentPane().add(setup);
        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public void init(int count){


        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel topp, middlep, bottomp, playerscontainer, players, healthcontainer, stepscontainer, blizzardcontainer;
        JLabel healthLabel, stepsLabel, blizzardLabel;

        topp = new JPanel();
        topp.setMaximumSize(new Dimension(800, 150));
        middlep = new JPanel();
        middlep.setMaximumSize(new Dimension(800, 300));
        bottomp = new JPanel();
        playerscontainer = new JPanel();
        players = new JPanel();
        healthcontainer = new JPanel();
        stepscontainer = new JPanel();
        blizzardcontainer = new JPanel();

        healthLabel = new JLabel("4");
        stepsLabel = new JLabel("2");
        blizzardLabel = new JLabel("3");

        //topp

        topp.setLayout(new BoxLayout(topp, BoxLayout.X_AXIS));
        topp.add(playerscontainer);
        topp.add(healthcontainer);
        topp.add(Box.createRigidArea(new Dimension(100, 0)));
        topp.add(stepscontainer);
        topp.add(Box.createRigidArea(new Dimension(100, 0)));
        topp.add(blizzardcontainer);

        playerscontainer.setLayout(new BoxLayout(playerscontainer, BoxLayout.Y_AXIS));
        playerscontainer.setMaximumSize(new Dimension(200, 150));
        playerscontainer.add(Box.createRigidArea(new Dimension(0, 30)));
        playerscontainer.add(new JLabel("Players"));
        playerscontainer.add(new JLabel("Itt lesznek a körök"));
        //playerscontainer.add(players);

        healthcontainer.setLayout(new BoxLayout(healthcontainer, BoxLayout.Y_AXIS));
        healthcontainer.add(new JLabel("Health"));
        healthcontainer.add(healthLabel);

        stepscontainer.setLayout(new BoxLayout(stepscontainer, BoxLayout.Y_AXIS));
        stepscontainer.add(new JLabel("Remaining steps"));
        stepscontainer.add(stepsLabel);

        blizzardcontainer.setLayout(new BoxLayout(blizzardcontainer, BoxLayout.Y_AXIS));
        blizzardcontainer.add(new JLabel("Blizzard comes in"));
        blizzardcontainer.add(blizzardLabel);

        //middlepp
        middlep.setSize(800, 350);
        middlep.setBackground(Color.blue);
        middlep.setBorder(new EmptyBorder(20, 20, 20, 20));
        middlep.setLayout(new GridLayout((int)Math.sqrt(count),(int)Math.sqrt(count), 20, 20));

        for(int i =0; i< count; i++){
            middlep.add(new FieldView());
        }

        //bottomp

        bottomp.setSize(800, 150);
        bottomp.setBackground(Color.black);
        bottomp.setLayout(new BoxLayout(bottomp, BoxLayout.X_AXIS));

        mainPanel.add(topp);
        mainPanel.add(middlep);
        mainPanel.add(bottomp);
    }

    public void actionPerformed(ActionEvent e){
        JButton b = (JButton)e.getSource();
        int i = Integer.valueOf(b.getName());
        init(i);
        cl.next(getContentPane());
    }
}
