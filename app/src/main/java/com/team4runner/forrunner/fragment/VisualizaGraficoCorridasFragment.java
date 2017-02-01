package com.team4runner.forrunner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.modelo.PontoRota;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

/**
 * Created by Lucas on 29/12/2015.
 */
public class VisualizaGraficoCorridasFragment extends Fragment {

    Corredor corredor;
    String dia;
    private TextView txtPse;
    private TextView txtClima;
    private TextView txtTerreno;
    private TextView txtObs;
    private TextView txtTemperatura;

    LineChartView chart;
    private PreviewLineChartView previewChart;
    private LineChartData data;
    private LineChartData previewData;

    private Corrida corrida;
    private List<PontoRota> pontos;


    //Listas para preencher o gr�fico
    private List<Double> mListDistancia;
    private List<Double> mListRitmo;
    private List<Double> mListDuracao;

    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_estatisticas_visualiza_corridas, container, false);

        ((VisualizarCorridaActivity)getActivity()).setFragGraf(this);

        corredor = ((VisualizarCorridaActivity) getActivity()).getCorredor();
        dia = ((VisualizarCorridaActivity) getActivity()).getDia();

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);

        chart = (LineChartView) fragment.findViewById(R.id.chart);
        previewChart = (PreviewLineChartView) fragment.findViewById(R.id.chart_preview);
        previewChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        txtPse = (TextView) fragment.findViewById(R.id.txtPSE);
        txtClima = (TextView) fragment.findViewById(R.id.txtClima);
        txtTerreno = (TextView) fragment.findViewById(R.id.txtTerreno);
        txtObs = (TextView) fragment.findViewById(R.id.txtObs);
        txtTemperatura = (TextView) fragment.findViewById(R.id.txtTemperatura);


        return fragment;
    }

    public void atualizarGrafico() {

        //Lista de pontos
        List<PointValue> values = new ArrayList<PointValue>();
        float ritmo;
        for (PontoRota ponto : pontos) {
            if (ponto.getVelocidade() != 0) {
                ritmo = (1000 / ponto.getVelocidade()) / 60;
                values.add(new PointValue(ponto.getDistancia(), ritmo));
            }
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setHasPoints(false);// too many values so don't draw points.

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        data = new LineChartData(lines);
        data.setAxisXBottom(new Axis());
        data.setAxisYLeft(new Axis().setHasLines(true));


        previewData = new LineChartData(data);
        previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
        previewChart.setPreviewColor(ChartUtils.COLOR_GREEN);
        previewChart.setLineChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());

        //Legenda
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName(getString(R.string.DistanciaMetros));
        axisY.setName(getString(R.string.RitmoMedioMK));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setLineChartData(data);
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);

        //PreviewX
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        previewChart.setCurrentViewport(tempViewport);
        previewChart.setZoomType(ZoomType.HORIZONTAL);


    }

    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart.
            chart.setCurrentViewport(newViewport);
        }

    }


    public void setCorrida(Corrida corrida, List<PontoRota> pontos){
        this.corrida = corrida;
        this.pontos = pontos;

        preencheCampos();

    }

    private void preencheCampos(){

        txtPse.setText(String.valueOf(corrida.getpSE()));
        txtClima.setText(corrida.getClima());
        txtTerreno.setText(corrida.getTerreno());
        txtObs.setText(corrida.getObs());
        txtTemperatura.setText(String.valueOf(corrida.getTemperatura()));

        atualizarGrafico();

        progressBar.setVisibility(View.GONE);

    }

}
