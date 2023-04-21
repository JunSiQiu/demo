<template>
  <div>
    <div style="display: flex; margin: 10px;">
      <div id="main" style="width: 600px;height:500px; background-color: aqua;"></div>
      <div id="second" style="width: 600px;height:500px; background-color: aqua;"></div>
    </div>

    <div style="display: flex; margin: 10px;">
      <div id="third" style="width: 600px;height:500px; background-color: aqua;"></div>
      <div id="earth" style="width: 600px;height:500px; background-color: aqua;"></div>
    </div>

  </div>
</template>

<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts-gl@2/dist/echarts-gl.min.js"></script>

<script>
import * as echarts from 'echarts';
import 'echarts-gl';

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  data() {
    return {
    }
  },
  methods: {

  },
  mounted() { // 页面渲染之后触发
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;

    option = {
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [128, 135, 84, 132, 98, 132, 133],
          type: 'line',
          lineStyle: {
            color: 'green',
          },
          label: {
            show: false,
            position: 'top',
            textStyle: {
              fontSize: 20,
              color: 'black'
            }
          },
          emphasis: {
            label: {
              show: true
            }
          }
        }
      ]
    };

    option && myChart.setOption(option);

    // 饼图
    var chartDom1 = document.getElementById('second');
    var myChart1 = echarts.init(chartDom1);
    var option1;

    option1 = {
      tooltip: {
        trigger: 'item'
      },
      legend: {
        top: '5%',
        left: 'center'
      },
      series: [
        {
          name: 'Access From',
          type: 'pie',
          radius: ['30%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 0.5
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 20,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            { value: 1048, name: 'Search Engine' },
            { value: 735, name: 'Direct' },
            { value: 580, name: 'Email' },
            { value: 484, name: 'Union Ads' },
            { value: 300, name: 'Video Ads' }
          ]
        }
      ]
    };

    option1 && myChart1.setOption(option1);

    //雷达图
    var chartDom2 = document.getElementById('third');
    var myChart2 = echarts.init(chartDom2);
    var option2;

    option2 = {
      title: {
        text: 'Basic Radar Chart'
      },
      legend: {
        data: ['Allocated Budget', 'Actual Spending']
      },
      radar: {
        // shape: 'circle',
        indicator: [
          { name: 'Sales', max: 6500 },
          { name: 'Administration', max: 16000 },
          { name: 'Information Technology', max: 30000 },
          { name: 'Customer Support', max: 38000 },
          { name: 'Development', max: 52000 },
          { name: 'Marketing', max: 25000 }
        ]
      },
      series: [
        {
          name: 'Budget vs spending',
          type: 'radar',
          data: [
            {
              value: [4200, 3000, 20000, 35000, 50000, 18000],
              name: 'Allocated Budget'
            },
            {
              value: [5000, 14000, 28000, 26000, 42000, 21000],
              name: 'Actual Spending'
            }
          ]
        }
      ]
    };

    option2 && myChart2.setOption(option2);

    // 3D
    var ROOT_PATH = 'https://echarts.apache.org/examples';

    var chartDom3 = document.getElementById('earth');
    var myChart3 = echarts.init(chartDom3);
    var option3;

    option3 = {
      backgroundColor: '#000',
      globe: {
        baseTexture: ROOT_PATH + '/data-gl/asset/world.topo.bathy.200401.jpg',
        heightTexture: ROOT_PATH + '/data-gl/asset/world.topo.bathy.200401.jpg',
        displacementScale: 0.04,
        shading: 'realistic',
        environment: ROOT_PATH + '/data-gl/asset/starfield.jpg',
        realisticMaterial: {
          roughness: 0.9
        },
        postEffect: {
          enable: true
        },
        light: {
          main: {
            intensity: 5,
            shadow: true
          },
          ambientCubemap: {
            texture: ROOT_PATH + '/data-gl/asset/pisa.hdr',
            diffuseIntensity: 0.2
          }
        }
      }
    };

    option3 && myChart3.setOption(option3);
  }
}

</script>