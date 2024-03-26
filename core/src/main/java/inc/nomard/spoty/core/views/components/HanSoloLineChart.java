package inc.nomard.spoty.core.views.components;

import eu.hansolo.fx.charts.*;
import eu.hansolo.fx.charts.data.XYChartItem;
import eu.hansolo.fx.charts.series.XYSeries;
import eu.hansolo.fx.charts.series.XYSeriesBuilder;
import javafx.animation.AnimationTimer;
import javafx.geometry.Orientation;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;

public class HanSoloLineChart extends AnchorPane {
    private static final Random RND = new Random();
    private static final Double AXIS_WIDTH = 25d;
    private XYChartItem p1;
    private XYChartItem p2;
    private XYChartItem p3;
    private XYChartItem p4;
    private XYChartItem p5;
    private XYChartItem p6;
    private XYChartItem p7;
    private XYChartItem p8;
    private XYChartItem p9;
    private XYChartItem p10;
    private XYChartItem p11;
    private XYChartItem p12;
    private XYSeries xySeries1;
    private XYSeries xySeries2;
    private Axis xAxisBottom;
    private Axis yAxisLeft;
    private XYChart<XYChartItem> lineChart;
    private long lastTimerCalled;
    private AnimationTimer timer;

    public HanSoloLineChart() {
        buildLineChart();
        startCharting();
    }

    private void buildLineChart() {
        p1 = new XYChartItem(1, RND.nextDouble() * 300 + 200, "Jan", "January");
        p2 = new XYChartItem(2, RND.nextDouble() * 300 + 200, "Feb", "February");
        p3 = new XYChartItem(3, RND.nextDouble() * 300 + 200, "Mar", "March");
        p4 = new XYChartItem(4, RND.nextDouble() * 300 + 200, "Apr", "April");
        p5 = new XYChartItem(5, RND.nextDouble() * 300 + 200, "May", "May");
        p6 = new XYChartItem(6, RND.nextDouble() * 300 + 200, "Jun", "June");
        p7 = new XYChartItem(7, RND.nextDouble() * 300 + 200, "Jul", "July");
        p8 = new XYChartItem(8, RND.nextDouble() * 300 + 200, "Aug", "August");
        p9 = new XYChartItem(9, RND.nextDouble() * 300 + 200, "Sep", "September");
        p10 = new XYChartItem(10, RND.nextDouble() * 300 + 200, "Oct", "October");
        p11 = new XYChartItem(11, RND.nextDouble() * 300 + 200, "Nov", "November");
        p12 = new XYChartItem(12, RND.nextDouble() * 300 + 200, "Dec", "December");


        xySeries1 = XYSeriesBuilder.create()
                .items(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
                .chartType(ChartType.SMOOTH_AREA)
                .fill(Color.web("#00AEF520"))
                .stroke(Color.web("#00AEF5"))
                .symbolFill(Color.web("#00AEF5"))
                .symbolStroke(Color.web("#293C47"))
                .symbolSize(10)
                .strokeWidth(3)
                .symbolsVisible(true)
                .build();

        xySeries2 = XYSeriesBuilder.create()
                .items(new XYChartItem(1, 280, "Jan", "January"),
                        new XYChartItem(2, 190, "Feb", "February"),
                        new XYChartItem(3, 280, "Mar", "March"),
                        new XYChartItem(4, 300, "Apr", "April"),
                        new XYChartItem(5, 205, "May", "May"),
                        new XYChartItem(6, 430, "Jun", "June"),
                        new XYChartItem(7, 380, "Jul", "July"),
                        new XYChartItem(8, 180, "Aug", "August"),
                        new XYChartItem(9, 300, "Sep", "September"),
                        new XYChartItem(10, 440, "Oct", "October"),
                        new XYChartItem(11, 300, "Nov", "November"),
                        new XYChartItem(12, 390, "Dec", "December"))
                .chartType(ChartType.SMOOTH_AREA)
                .fill(Color.web("#4EE29B20"))
                .stroke(Color.web("#4EE29B"))
                .symbolFill(Color.web("#4EE29B"))
                .symbolStroke(Color.web("#293C47"))
                .symbolSize(10)
                .strokeWidth(3)
                .symbolsVisible(true)
                .build();

        xAxisBottom = AxisBuilder.create(Orientation.HORIZONTAL, Position.BOTTOM)
                .type(AxisType.TEXT)
                .prefHeight(AXIS_WIDTH)
                .categories("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                .minValue(1)
                .maxValue(13)
                .autoScale(true)
                .axisColor(Color.web("#85949B"))
                .tickLabelColor(Color.web("#85949B"))
                .tickMarkColor(Color.web("#85949B"))
                //.tickMarksVisible(false)
                .build();
        AnchorPane.setBottomAnchor(xAxisBottom, 0d);
        AnchorPane.setLeftAnchor(xAxisBottom, AXIS_WIDTH);
        AnchorPane.setRightAnchor(xAxisBottom, AXIS_WIDTH);

        yAxisLeft = AxisBuilder.create(Orientation.VERTICAL, Position.LEFT)
                .type(AxisType.LINEAR)
                .prefWidth(AXIS_WIDTH)
                .minValue(0)
                .maxValue(1000)
                //.autoScale(true)
                .axisColor(Color.web("#85949B"))
                .tickLabelColor(Color.web("#85949B"))
                .tickMarkColor(Color.web("#85949B"))
                //.tickMarksVisible(false)
                // test the new numberFormatter as well
                .numberFormatter(new StringConverter<Number>() {
                    private final DecimalFormat df = new DecimalFormat("##0 m");

                    @Override
                    public String toString(Number object) {
                        if (object == null) {
                            return "";
                        }
                        return df.format(object);
                    }

                    @Override
                    public Number fromString(String string) {
                        try {
                            if (string == null) {
                                return null;
                            }
                            string = string.trim();
                            if (string.length() < 1) {
                                return null;
                            }
                            return df.parse(string).doubleValue();
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                })
                .build();
        AnchorPane.setTopAnchor(yAxisLeft, 0d);
        AnchorPane.setBottomAnchor(yAxisLeft, AXIS_WIDTH);
        AnchorPane.setLeftAnchor(yAxisLeft, 0d);

        Grid grid = GridBuilder.create(xAxisBottom, yAxisLeft)
                .gridLinePaint(Color.web("#384C57"))
                .minorHGridLinesVisible(false)
                .mediumHGridLinesVisible(false)
                .minorVGridLinesVisible(false)
                .mediumVGridLinesVisible(false)
                .gridLineDashes(4, 4)
                .build();

        XYPane lineChartPane = new XYPane(xySeries1, xySeries2);

        lineChart = new XYChart<>(lineChartPane, grid, yAxisLeft, xAxisBottom);
        AnchorPane.setTopAnchor(lineChart, 0d);
        AnchorPane.setBottomAnchor(lineChart, 0d);
        AnchorPane.setLeftAnchor(lineChart, 0d);
        AnchorPane.setRightAnchor(lineChart, 0d);

        lastTimerCalled = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                if (now > lastTimerCalled + 2_000_000_000l) {
                    p1.setY(RND.nextDouble() * 300 + 200);
                    p2.setY(RND.nextDouble() * 300 + 200);
                    p3.setY(RND.nextDouble() * 300 + 200);
                    p4.setY(RND.nextDouble() * 300 + 200);
                    p5.setY(RND.nextDouble() * 300 + 200);
                    p6.setY(RND.nextDouble() * 300 + 200);
                    p7.setY(RND.nextDouble() * 300 + 200);
                    p8.setY(RND.nextDouble() * 300 + 200);
                    p9.setY(RND.nextDouble() * 300 + 200);
                    p10.setY(RND.nextDouble() * 300 + 200);
                    p11.setY(RND.nextDouble() * 300 + 200);
                    p12.setY(RND.nextDouble() * 300 + 200);

                    // test the new numberFormatter as well
                    yAxisLeft.setMinMax(RND.nextDouble() * 300, 1000 - RND.nextDouble() * 300);

                    lastTimerCalled = now;
                }
            }
        };
    }

    private XYSeries createSeries() {
        XYSeries xySeries = XYSeriesBuilder.create()
                // add tooltip to chart items to test css styling
                .items(new XYChartItem(1, 600, "Jan", "January"),
                        new XYChartItem(2, 760, "Feb", "February"),
                        new XYChartItem(3, 585, "Mar", "March"),
                        new XYChartItem(4, 410, "Apr", "April"),
                        new XYChartItem(5, 605, "May", "May"),
                        new XYChartItem(6, 825, "Jun", "June"),
                        new XYChartItem(7, 595, "Jul", "July"),
                        new XYChartItem(8, 300, "Aug", "August"),
                        new XYChartItem(9, 515, "Sep", "September"),
                        new XYChartItem(10, 780, "Oct", "October"),
                        new XYChartItem(11, 570, "Nov", "November"),
                        new XYChartItem(12, 620, "Dec", "December"))
                .chartType(ChartType.SMOOTH_AREA)
                .fill(Color.web("#AE00F520"))
                .stroke(Color.web("#AE00F5"))
                .symbolFill(Color.web("#AE00F5"))
                .symbolStroke(Color.web("#293C47"))
                .symbolSize(10)
                .strokeWidth(3)
                .symbolsVisible(true)
                .build();
        return xySeries;
    }

    private Axis createLeftYAxis(final double MIN, final double MAX, final boolean AUTO_SCALE, final double AXIS_WIDTH) {
        Axis axis = new Axis(Orientation.VERTICAL, Position.LEFT);
        axis.setMinValue(MIN);
        axis.setMaxValue(MAX);
        axis.setPrefWidth(AXIS_WIDTH);
        axis.setAutoScale(AUTO_SCALE);

        AnchorPane.setTopAnchor(axis, 0d);
        AnchorPane.setBottomAnchor(axis, AXIS_WIDTH);
        AnchorPane.setLeftAnchor(axis, 0d);

        return axis;
    }

    private Axis createCenterYAxis(final double MIN, final double MAX, final boolean AUTO_SCALE, final double AXIS_WIDTH) {
        Axis axis = new Axis(Orientation.VERTICAL, Position.CENTER);
        axis.setMinValue(MIN);
        axis.setMaxValue(MAX);
        axis.setPrefWidth(AXIS_WIDTH);
        axis.setAutoScale(AUTO_SCALE);

        AnchorPane.setTopAnchor(axis, 0d);
        AnchorPane.setBottomAnchor(axis, AXIS_WIDTH);
        AnchorPane.setLeftAnchor(axis, axis.getZeroPosition());

        return axis;
    }

    private Axis createRightYAxis(final double MIN, final double MAX, final boolean AUTO_SCALE, final double AXIS_WIDTH) {
        Axis axis = new Axis(Orientation.VERTICAL, Position.RIGHT);
        axis.setMinValue(MIN);
        axis.setMaxValue(MAX);
        axis.setPrefWidth(AXIS_WIDTH);
        axis.setAutoScale(AUTO_SCALE);

        AnchorPane.setRightAnchor(axis, 0d);
        AnchorPane.setTopAnchor(axis, 0d);
        AnchorPane.setBottomAnchor(axis, AXIS_WIDTH);

        return axis;
    }

    private Axis createBottomXAxis(final double MIN, final double MAX, final boolean AUTO_SCALE, final double AXIS_WIDTH) {
        Axis axis = new Axis(Orientation.HORIZONTAL, Position.BOTTOM);
        axis.setMinValue(MIN);
        axis.setMaxValue(MAX);
        axis.setPrefHeight(AXIS_WIDTH);
        axis.setAutoScale(AUTO_SCALE);

        AnchorPane.setBottomAnchor(axis, 0d);
        AnchorPane.setLeftAnchor(axis, AXIS_WIDTH);
        AnchorPane.setRightAnchor(axis, AXIS_WIDTH);

        return axis;
    }

    private Axis createCenterXAxis(final double MIN, final double MAX, final boolean AUTO_SCALE, final double AXIS_WIDTH) {
        Axis axis = new Axis(Orientation.HORIZONTAL, Position.CENTER);
        axis.setMinValue(MIN);
        axis.setMaxValue(MAX);
        axis.setPrefHeight(AXIS_WIDTH);
        axis.setAutoScale(AUTO_SCALE);

        AnchorPane.setBottomAnchor(axis, axis.getZeroPosition());
        AnchorPane.setLeftAnchor(axis, AXIS_WIDTH);
        AnchorPane.setRightAnchor(axis, AXIS_WIDTH);

        return axis;
    }

    private Axis createTopXAxis(final double MIN, final double MAX, final boolean AUTO_SCALE, final double AXIS_WIDTH) {
        Axis axis = new Axis(Orientation.HORIZONTAL, Position.TOP);
        axis.setMinValue(MIN);
        axis.setMaxValue(MAX);
        axis.setPrefHeight(AXIS_WIDTH);
        axis.setAutoScale(AUTO_SCALE);

        AnchorPane.setTopAnchor(axis, AXIS_WIDTH);
        AnchorPane.setLeftAnchor(axis, AXIS_WIDTH);
        AnchorPane.setRightAnchor(axis, AXIS_WIDTH);

        return axis;
    }

    public void startCharting() {
        this.getChildren().add(lineChart);
    }
}
