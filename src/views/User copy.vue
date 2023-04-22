<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" prefix-icon="el-icon-search" placeholder="请输入名称" v-model="username"></el-input>
      <el-input style="width: 200px" prefix-icon="el-icon-position" placeholder="请输入地址" class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" prefix-icon="el-icon-message" placeholder="请输入邮箱" class="ml-5" v-model="address"></el-input>
      <el-button style="margin-left: 5px" @click="load" type="primary">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleApp">新增 <i class="el-icon-plus"></i></el-button>
      <el-popconfirm
          class="ml-5 mr-5"
          confirm-button-text='确定'
          cancel-button-text='再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-minus"></i></el-button>
      </el-popconfirm>
      <el-upload action="http://localhost:8081/user/import" :show-file-list="false" accept="xlsx"
                 :on-success="handleExcelImportSuccess" style="display: inline-block" class="mr-5">
        <el-button type="primary">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="120"></el-table-column>
      <el-table-column prop="username" label="用户名" width="140"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-minus"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "User",
  data() {
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:10,
      username:"",
      email:"",
      address:"",
      dialogFormVisible:false,
      form:{},
      multipleSelection:[],
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      // 请求分页查询数据
      this.request.get("/user/page", {
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username,
          email:this.email,
          address:this.address
        }
      }).then(res => {
        console.log(res.data)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save(){
      this.request.post("/user/save",this.form).then(res => {
        if(res){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.reset()
        }else
          this.$message.error("保存失败")
      })
    },
    handleApp(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    del(id){
      this.request.delete("/user/"+id).then(res => {
        console.log(res)
        if(res.data){
          this.$message.success("删除成功")
          this.dialogFormVisible = false
          this.reset()
        }else
          this.$message.error("删除失败")
      })
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)     //[{},{},{}] => [1,2,3]
      this.request.post("/user/del/batch",ids).then(res => {
        if(res.data){
          this.$message.success("批量删除成功")
          this.dialogFormVisible = false
          this.reset()
        }else
          this.$message.error("批量删除失败")
      })
    },
    reset(){
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    // 页面显示数量(默认10条)
    handleSizeChange(pageSize){
      console.log("页面显示数量:"+pageSize)
      this.pageSize = pageSize
      this.load()
    },
    // 获取当前页数
    handleCurrentChange(pageNum){
      console.log("当前页数:"+pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp(){
      window.open("http://localhost:8081/user/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("导入成功")
      this.load()
    },
  }
}
</script>

<style>
.headerBg{
  background-color: #eee!important;
}
</style>
