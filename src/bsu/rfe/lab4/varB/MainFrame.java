package bsu.rfe.lab4.varB;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class MainFrame extends JFrame {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private JFileChooser fileChooser = null;

	private JCheckBoxMenuItem showAxisMenuItem;
	private JCheckBoxMenuItem showMarkersMenuItem;
	private JCheckBoxMenuItem showSpGraphicsMenuItem;

	private GraphicsDisplay display = new GraphicsDisplay();

	private boolean fileLoaded = false;

	protected void openGraphics(File selectedFile) {
	  try {
		
	    DataInputStream in = new DataInputStream(
	    new FileInputStream(selectedFile));
	
	    Double[][] graphicsData = new Double[in.available()/(Double.SIZE/8)/2][];

	    int i = 0;
	    while (in.available()>0) {

	     Double x = in.readDouble();

	      Double y = in.readDouble();

	      graphicsData[i++] = new Double[] {x, y};
	    }

	      if (graphicsData!=null && graphicsData.length>0) {

	      fileLoaded = true;

	      display.showGraphics(graphicsData);
	   }

	   in.close();
	  } catch (FileNotFoundException ex) {

	    JOptionPane.showMessageDialog(MainFrame.this,
	    "Указанный файл не найден", "Ошибка загрузки данных",
	    JOptionPane.WARNING_MESSAGE);
	    return;
	  } catch (IOException ex) {

	    JOptionPane.showMessageDialog(MainFrame.this,
	    "Ошибка чтения координат точек из файла",
	    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
	    return;
	  }
	}

	private class GraphicsMenuListener implements MenuListener {

		public void menuSelected(MenuEvent e) {
			
		showAxisMenuItem.setEnabled(fileLoaded);
		showMarkersMenuItem.setEnabled(fileLoaded);
		showSpGraphicsMenuItem.setEnabled(fileLoaded);
		}

		public void menuDeselected(MenuEvent e) {
		}

		public void menuCanceled(MenuEvent e) {
		}
		}

	public MainFrame() {
		super("Построение графиков функций на основе подготовленных файлов и обработка событий мыши");

		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();

		setLocation((kit.getScreenSize().width - WIDTH)/2,
		(kit.getScreenSize().height - HEIGHT)/2);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Файл");
		menuBar.add(fileMenu);

		Action openGraphicsAction = new AbstractAction("Открыть файл") {
		  public void actionPerformed(ActionEvent event) {
		    if (fileChooser==null) {
		      fileChooser = new JFileChooser();
		      fileChooser.setCurrentDirectory(new File("."));
		    }
		    if (fileChooser.showOpenDialog(MainFrame.this) ==
		      JFileChooser.APPROVE_OPTION)
		      openGraphics(fileChooser.getSelectedFile());
		    }
		};

		fileMenu.add(openGraphicsAction);

		JMenu graphicsMenu = new JMenu("График");
		menuBar.add(graphicsMenu);
		graphicsMenu.addMenuListener(new GraphicsMenuListener());
		
		Action showAxisAction = new AbstractAction("Показывать оси координат") {
			public void actionPerformed(ActionEvent event) {

			display.setShowAxis(showAxisMenuItem.isSelected());
			}
			};
			showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);

			graphicsMenu.add(showAxisMenuItem);

			showAxisMenuItem.setSelected(true);
			
			Action showSpGraphicsAction = new AbstractAction("Показать график |f(x)|") {
				public void actionPerformed(ActionEvent event) {

				display.setShowSpGraphics(showSpGraphicsMenuItem.isSelected());
				}
				};
				showSpGraphicsMenuItem = new JCheckBoxMenuItem(showSpGraphicsAction);

				graphicsMenu.add(showSpGraphicsMenuItem);
				
				showAxisMenuItem.setSelected(false);

			Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
			public void actionPerformed(ActionEvent event) {

			display.setShowMarkers(showMarkersMenuItem.isSelected());
			}
			};
			showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
			graphicsMenu.add(showMarkersMenuItem);

			showMarkersMenuItem.setSelected(false);
			
			
			getContentPane().add(display, BorderLayout.CENTER);


	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
