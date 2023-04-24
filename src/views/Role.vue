<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" prefix-icon="el-icon-search" placeholder="请输入名称" v-model="name"></el-input>
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
    </div>

    <el-table :data="tableData" border stripe header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="flag" label="唯一标识"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu"></i></el-button>
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
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVisible" width="30%">
      <el-tree
        :data="menuData"
        show-checkbox
        node-key="id"
        ref="tree"
        :check-strictly="true"
        :default-expanded-keys="expends"
        :default-checked-keys="checks"
        :props="defaultProps">
        <span class="custom-tree-node" slot-scope="{ data }">
          <span><i :class="data.icon"/>{{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import store from '@/store'

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "User",
  data() {
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:10,
      name:"",
      dialogFormVisible:false,
      menuDialogVisible:false,
      form:{},
      multipleSelection:[],
      menuData:[],
      defaultProps:{
        label:"name"
      },
      expends:[],
      checks:[],
      roleId:0,
      roleFlag:''
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      // 请求分页查询数据
      this.request.get("/role/page", {
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name,
        }
      }).then(res => {
        console.log(res.data)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save(){
      this.request.post("/role/save",this.form).then(res => {
        if(res.code == '200'){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.reset()
        }else
          this.$message.error("保存失败")
      })
    },
    saveRoleMenu (){
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if(res.code == '200'){
          this.$message.success("绑定成功")
          this.menuDialogVisible = false
          // 操作管理员角色后需要重新登录
          if(this.roleFlag === "ROLE_ADMIN"){
            store.commit("logout")
          }
        }else {
          this.$message.error(res.msg)
        }
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
      this.request.delete("/role/"+id).then(res => {
        console.log(res)
        if(res.code == '200'){
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
      this.request.post("/role/del/batch",ids).then(res => {
        if(res.code == '200'){
          this.$message.success("批量删除成功")
          this.dialogFormVisible = false
          this.reset()
        }else
          this.$message.error("批量删除失败")
      })
    },
    reset(){
      this.name = ""
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
    selectMenu(role) {
      this.menuDialogVisible = true
      this.roleId = role.id
      this.roleFlag = role.flag
      // 请求菜单数据
      this.request.get("/menu").then(res => {
        this.menuData = res.data
        //把后台返回的菜单数据处理成 id数组
        this.expends =  this.menuData.map(v => v.id)
      })

      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data
      })
    }
  }
}
</script>

<style>
.headerBg{
  background-color: #eee!important;
}
</style>
