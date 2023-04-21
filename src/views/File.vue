<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" prefix-icon="el-icon-search" placeholder="请输入名称" 
                v-model="name"></el-input>
            <el-button style="margin-left: 5px" @click="load" type="primary">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-upload action="http://localhost:8081/file/upload" :show-file-list="false"
                :on-success="handleFileUploadSuccess" style="display: inline-block" class="mr-5">
                <el-button type="primary">上传 <i class="el-icon-top"></i></el-button>
            </el-upload>
            <el-popconfirm class="ml-5 mr-5" confirm-button-text='确定' cancel-button-text='再想想' icon="el-icon-info"
                icon-color="red" title="您确定删除这些数据吗？" @confirm="delBatch">
                <el-button type="danger" slot="reference">批量删除 <i class="el-icon-minus"></i></el-button>
            </el-popconfirm>
        </div>

        <el-table :data="tableData" border stripe header-cell-class-name="headerBg"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection"></el-table-column>
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="name" label="文件名称" width="250"></el-table-column>
            <el-table-column prop="type" label="文件类型"></el-table-column>
            <el-table-column prop="size" label="文件大小（kb）"></el-table-column>
            <el-table-column label="下载">
                <template slot-scope="scope">
                    <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="enable" label="启用">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"
                        @change="changeEnable(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-popconfirm class="ml-5" confirm-button-text='确定' cancel-button-text='再想想' icon="el-icon-info"
                        icon-color="red" title="您确定删除吗？" @confirm="del(scope.row.id)">
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-minus"></i></el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <div style="padding: 10px 0">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
                :page-sizes="[2, 5, 10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
        </div>

    </div>
</template>

<script>
export default {
    // eslint-disable-next-line vue/multi-word-component-names
    name: "File",
    data() {
        return {
            tableData: [],
            total: 0,
            pageNum: 1,
            pageSize: 10,
            name: '',
            multipleSelection: []
        }
    },
    created() {
        this.load();
    },
    methods: {
        load() {
            // 请求分页查询数据
            this.request.get("/file/page", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                }
            }).then(res => {
                console.log(res.data)
                this.tableData = res.data.records
                this.total = res.data.total
            })
        },

        changeEnable(row) {
            this.request.post("/file/update", row).then(res => {
                if (res.code == '200')
                    this.$message.success("操作成功")
            })
        },

        handleApp() {
            this.dialogFormVisible = true
            this.form = {}
        },
        handleEdit(row) {
            this.form = row
            this.dialogFormVisible = true
        },
        del(id) {
            this.request.delete("/file/" + id).then(res => {
                console.log(res)
                if (res.code == '200') {
                    this.$message.success("删除成功")
                    this.dialogFormVisible = false
                    this.reset()
                } else
                    this.$message.error("删除失败")
            })
        },
        handleSelectionChange(val) {
            console.log(val)
            this.multipleSelection = val
        },
        delBatch() {
            let ids = this.multipleSelection.map(v => v.id)     //[{},{},{}] => [1,2,3]
            this.request.post("/file/del/batch", ids).then(res => {
                if (res.code == '200') {
                    this.$message.success("批量删除成功")
                    this.dialogFormVisible = false
                    this.reset()
                } else
                    this.$message.error("批量删除失败")
            })
        },
        reset() {
            this.name = ""
            this.load()
        },
        // 页面显示数量(默认10条)
        handleSizeChange(pageSize) {
            console.log("页面显示数量:" + pageSize)
            this.pageSize = pageSize
            this.load()
        },
        // 获取当前页数
        handleCurrentChange(pageNum) {
            console.log("当前页数:" + pageNum)
            this.pageNum = pageNum
            this.load()
        },
        handleFileUploadSuccess(res) {
            console.log(res)
            this.load()
        },

        download(url) {
            window.open(url)
        }
    }
}
</script>