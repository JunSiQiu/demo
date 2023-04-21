<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px;padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px;"><b>登录</b></div>
      <!--表单验证-->
      <el-form :model="user" :rules="rules" ref="userFrom">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
      </el-form>

      <div style="margin: 10px 0; text-align: right">
        <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
        <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  data() {
    return {
      user:{},
      rules:{
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
      },
      tableData: [{
        value:false
      }],
      dialogImageUrl: '',
      dialogVisible: false
    }
  },
  methods: {
    login(){
      this.$refs['userFrom'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            console.log(res)
            if(res.code === '200'){
              localStorage.setItem("user",JSON.stringify(res.data))  // 存储用户信息到浏览器
              this.$router.push("/home")
              this.$message.success("登录成功")
            } else
              this.$message.error(res.msg)
          })
        }
      });
    },
  }
}
</script>

<style scoped>
  .wrapper{
    height: 100vh;
    background-image: linear-gradient(to bottom right,#FC466B, #3F5EFB);
    overflow: hidden;
  }
</style>
