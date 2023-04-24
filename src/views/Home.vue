<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px;">
      <el-col :span="6">
        <el-card style="color: #409EFF;">
          <div><i class="el-icon-user-solid" />用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C;">
          <div><i class="el-icon-money" />销售总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            ￥ 10000000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A;">
          <div><i class="el-icon-bank-card" />收益总额</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            ￥ 3000000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C;">
          <div><i class="el-icon-s-shop" />门店总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;">
            100
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 500px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 500px;"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import * as echarts from 'echarts';

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Home",
  data() {
    return {

    }
  },
  mounted() {      //页面渲染后再触发
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;
    option = {
      title: {
        text: "各季度用户趋势",
        subtext: "趋势图",
        left: "center"
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar',
          itemStyle: {
            color: '#91cc75',
          }
        }
      ]
    };
    this.request.get("/echarts/members").then(res => {
      // console.log(res)
      // 填空
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data;
      option.series[1].data = res.data;
      // 数据准备完后再set
      myChart.setOption(option);
    })

    // 饼图
    var chartDom1 = document.getElementById('pie');
    var myChart1 = echarts.init(chartDom1);

    var option1 = {
      title: {
        text: '各季度用户统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'tiem',
        formatter: '{d}%'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          
          label: {               // 饼图图形上的文本标签
            normal: {
              show: true,
              position: 'inner', // 标签位置
              textStyle: {
                fontWeight: 300,
                fontSize: 14    // 文字大小
              },
              formatter: '{b}:({d}%)'
            }
          },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          // itemStyle: {
          //   barBorderRadius: 10,
          //   borderColor: '#fff',
          //   borderWidth: 1
          // },
        }
      ]
    };
    this.request.get("/echarts/members").then(res => {
      // console.log(res)
      option1.series[0].data = [
        { name: "第一季度", value: res.data[0] },
        { name: "第二季度", value: res.data[1] },
        { name: "第三季度", value: res.data[2] },
        { name: "第四季度", value: res.data[3] },
      ]
      myChart1.setOption(option1);
    })
  }
}
</script>

<style scoped></style>
