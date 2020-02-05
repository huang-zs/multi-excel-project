<template>
  <div>
    <h1>test3</h1>
  </div>
</template>
<script>
export default {
  name: 'test3',
  mounted () {
    this.webSocketInit()
  },
  methods: {
    webSocketInit () {
      console.log('websocket初始化')
      this.webSocket = new WebSocket('ws://localhost:8081/multi-user-excel-system/ws/asset/test')
      // 连接打开事件
      this.webSocket.onopen = function () {
        window.console.log('Socket 已打开')
      }
      // 连接关闭事件
      this.webSocket.onclose = function () {
        window.console.log('Socket已关闭')
      }
      // 发生了错误事件
      this.webSocket.onerror = function () {
        alert('Socket发生了错误')
      }
      // 收到消息事件
      this.webSocket.onmessage = function (msg) {
        console.log('收到信息')
      }
      // 窗口关闭时，关闭连接
      window.unload = function () {
        this.webSocket.close()
      }
    }
  }

}
</script>
<style scoped>
.sample-tutorial {
  position: relative;
  height: 100%;
  overflow: hidden;
}
</style>
