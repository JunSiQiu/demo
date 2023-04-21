<template>
    <el-card style="width: 500px;">
        <el-form label-width="60px" size="small">

            <el-upload class="avatar-uploader" action="http://localhost:8081/file/upload" :show-file-list="false"
                :on-success="handleAvatarSuccess">
                <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>

            <el-form-item label="用户名">
                <el-input v-model="form.username" autocomplete="off" disabled></el-input>
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
                <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="save">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
export default {
    // eslint-disable-next-line vue/multi-word-component-names
    name: "Person",
    data() {
        return {
            form: {},
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
        }
    },
    created() {
        this.getUser().then(res => {
            //console.log(res)
            this.form = res
        })
    },
    methods: {
        async getUser() {
            return (await this.request.get("/user/username/" + this.user.username)).data
        },
        save() {
            console.log(this.user)
            this.request.post("/user/save", this.form).then(res => {
                if (res) {
                    this.$message.success("保存成功")
                    // 触发父级更新Uesr
                    this.$emit("refreshUser")
                    // 更新浏览器缓存

                } else
                    this.$message.error("保存失败")
            })
        },
        handleAvatarSuccess(res) {
            this.form.avatarUrl = res
        }
    }
}
</script>

<style>
.avatar-uploader {
    text-align: center;
    margin-bottom: 20px;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    text-align: center;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 138px;
    height: 138px;
    line-height: 138px;
    text-align: center;
}

.avatar {
    width: 138px;
    height: 138px;
    display: block;
}
</style>