<template>
  <div class="mainFrame">
    <el-carousel :interval="4000" type="card" height="500px">
      <el-carousel-item v-for="item in doorImageArr" :key="item.name">
        <el-card :body-style="{ padding: '20px' }">
          <div slot="header" class="clearfix">
            <span>{{item.name}}</span>
          </div>
          <img :src="item.url" style="width:100%" />
          <div style="padding: 14px;">
            <span>{{item.content}}</span>
          </div>
        </el-card>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
import { getDoorImages } from '@/api/door'
export default {
  data() {
    return {
      doorImageArr: []
    }
  },
  mounted() {
    console.log('mounted')
    //获取门户图片
    getDoorImages().then(response => {
      console.log(response)

      this.doorImageArr = response.data.data
    })
  },

}
</script>
