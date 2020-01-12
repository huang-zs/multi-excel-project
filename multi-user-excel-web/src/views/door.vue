<template>
  <el-row>
    <el-col :span="20">
      <el-carousel :interval="4000" type="card" height="400px">
        <el-carousel-item>
          <img src="../assets/blue.png" style="width:100%" />
        </el-carousel-item>
        <el-carousel-item v-for="item in doorImageArr" :key="item">
          <h1>{{item}}</h1>
          <img :src="item" style="width:100%" />
          <!-- <img src="../assets/blue.png" style="width:100%" /> -->
        </el-carousel-item>
      </el-carousel>
    </el-col>
    <el-col :span="4">
      <div>
        <el-button type>游客模式</el-button>
      </div>
      <div>
        <el-button type @click="toLogin">用户模式</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  data () {
    return {
      doorImageArr: [
        '../assets/blue.png',
        '../assets/pink.png',
        '../assets/red.png',
        '../assets/yellow.png'
      ]
    }
  },
  methods: {
    toLogin () {
      this.$router.push('/user/login')
    }
  }
}
</script>
