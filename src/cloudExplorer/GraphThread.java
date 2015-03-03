/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package cloudExplorer;

import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Plot;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.ScatterPlot;
import com.googlecode.charts4j.XYLineChart;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GraphThread implements Runnable {

    Thread getThread;
    NewJFrame mainFrame;
    String Home = System.getProperty("user.home");
    String[] object = new String[1];
    String temp_file = (Home + File.separator + "object.tmp");
    String what = null;
    Put put;
    double[] x;
    double[] y;
    String x_whattograph_field;
    String y_whattograph_field;
    String graph_name_field;
    String x_name_field;
    String y_name_field;
    String x_graphsize_field;
    String y_graphsize_field;
    File check_temp = new File(temp_file);
    Thread gt;
    Boolean first_pass = true;
    Boolean proceed = true;
    boolean line = true;

    public GraphThread(NewJFrame Frame, String Awhat, String Agraph_name_field, String xx_whattograph_field, String yy_whattograph_field, String xx_name_field, String yy_name_field, String xx_graphsize_field, String yy_graphsize_field, Boolean ALine) {
        mainFrame = Frame;
        what = Awhat;
        line = ALine;
        graph_name_field = Agraph_name_field;
        x_whattograph_field = xx_whattograph_field;
        y_whattograph_field = yy_whattograph_field;
        x_name_field = xx_name_field;
        y_name_field = yy_name_field;
        x_graphsize_field = xx_graphsize_field;
        y_graphsize_field = yy_graphsize_field;

    }

    void calibrateTextArea() {
        NewJFrame.jTextArea1.append("\n");
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void get_csv() {

        mainFrame.jTextArea1.append("\nDownloading data......");
        calibrateTextArea();
        File tempFile = new File(temp_file);
        Get get = new Get(what, mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), temp_file, null);
        get.run();
    }

    void process_data() {
        mainFrame.jTextArea1.append("\nProcessing data......");
        calibrateTextArea();

        try {
            FileReader frr = new FileReader(temp_file);
            BufferedReader bfrr = new BufferedReader(frr);
            String read = null;
            int i = 0;
            while ((read = bfrr.readLine()) != null) {
                if (!first_pass) {
                    int XwhatToGraph = Integer.parseInt(x_whattograph_field);
                    int YwhatToGraph = Integer.parseInt(y_whattograph_field);
                    String[] parse = read.split(",");
                    if (parse[XwhatToGraph].contains(":")) {
                        String[] cut = parse[XwhatToGraph].split(":");
                        parse[XwhatToGraph] = cut[0];
                    }
                    if (parse[YwhatToGraph].contains(":")) {
                        String[] cut = parse[YwhatToGraph].split(":");
                        parse[YwhatToGraph] = cut[0];
                    }
                    x[i] = Double.parseDouble(parse[XwhatToGraph]);
                    y[i] = Double.parseDouble(parse[YwhatToGraph]);
                    if (proceed) {
                        graph();
                    }
                }
                i++;
            }
            if (first_pass) {
                x = new double[i];
                y = new double[i];
                first_pass = false;
                process_data();
            } else {
                first_pass = true;
            }
            bfrr.close();
        } catch (Exception tempFile) {
            proceed = false;
            mainFrame.jTextArea1.append("\nError importing data. Please ensure the fields are correct.");
            calibrateTextArea();
        }

    }

    public void graph() {
        mainFrame.jTextArea1.append("\nGraphing......");
        calibrateTextArea();
        try {
            Data xdata = DataUtil.scaleWithinRange(0, x[1] * 4, x);
            Data ydata = DataUtil.scaleWithinRange(0, y[1] * 4, y);
            Plot plot = Plots.newXYLine(xdata, ydata);
            plot.setColor(com.googlecode.charts4j.Color.BLUE);
            ImageIcon throughput_icon;

            if (line) {
                XYLineChart xyLineChart = GCharts.newXYLineChart(plot);
                xyLineChart.setSize(Integer.parseInt(x_graphsize_field), Integer.parseInt(y_graphsize_field));
                xyLineChart.setTitle(graph_name_field);
                xyLineChart.addXAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", x_name_field)));
                xyLineChart.addYAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", y_name_field)));
                xyLineChart.addXAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(0, x[1] * 4));
                xyLineChart.addYAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(0, y[1] * 4));
                throughput_icon = (new ImageIcon(ImageIO.read(new URL(xyLineChart.toURLString()))));
            } else {
                ScatterPlot Scatteredplot = GCharts.newScatterPlot(plot);
                Scatteredplot.setSize(Integer.parseInt(x_graphsize_field), Integer.parseInt(y_graphsize_field));
                Scatteredplot.setTitle(graph_name_field);
                Scatteredplot.addXAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", x_name_field)));
                Scatteredplot.addYAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", y_name_field)));
                Scatteredplot.addXAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(0, x[1] * 4));
                Scatteredplot.addYAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(0, y[1] * 4));
                throughput_icon = (new ImageIcon(ImageIO.read(new URL(Scatteredplot.toURLString()))));
            }

            JLabel label_throughput = new JLabel(throughput_icon);

            //Configures the panel
            NewJFrame.jPanel11.removeAll();
            GridLayout layout = new GridLayout(0, 3);
            NewJFrame.jPanel11.setLayout(layout);

            NewJFrame.jPanel11.add(label_throughput);
            try {
                Image image_throughput = throughput_icon.getImage();
                BufferedImage buffered_throughput_icon = (BufferedImage) image_throughput;
                File outputfile = new File(Home + File.separator + "GRAPH-" + graph_name_field + ".png");
                ImageIO.write(buffered_throughput_icon, "png", outputfile);
                if (outputfile.exists()) {
                    mainFrame.jTextArea1.append("\nSaved graph to: " + Home + File.separator + "GRAPH-" + graph_name_field + ".png");
                    calibrateTextArea();
                }
            } catch (Exception ex) {

            }

            NewJFrame.jPanel11.revalidate();
            NewJFrame.jPanel11.repaint();
            System.gc();
        } catch (Exception graph) {
            System.out.print("\nDebug 1:" + graph.getMessage());
            proceed = false;
        }
    }

    public void run() {
        File check_what = new File(what);

        if (check_temp.exists()) {
            check_temp.delete();
        }

        get_csv();

        if (check_temp.exists()) {
            process_data();

        }
    }

    void startc(NewJFrame Frame, String Awhat, String Agraph_name_field, String xx_whattograph_field, String yy_whattograph_field, String xx_name_field, String yy_name_field, String xx_graphsize_field, String yy_graphsize_field, Boolean ALine) {
        {
            (new Thread(new GraphThread(Frame, Awhat, Agraph_name_field, xx_whattograph_field, yy_whattograph_field, xx_name_field, yy_name_field, xx_graphsize_field, yy_graphsize_field, ALine))).start();
        }
    }
}
