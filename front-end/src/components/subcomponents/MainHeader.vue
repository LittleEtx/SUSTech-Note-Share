<template>
<div class="header-align">
    <div class="icon">
        <img src="../../assets/icon/icon_with_word.svg"
             style="height: 50px; float: left;"  alt="">
    </div>
    <div class="section">
        <icon-button icon="el-icon-menu" style="float: right">全部分区</icon-button>
    </div>
    <el-input placeholder="搜索笔记" class="search-bar" suffix-icon="el-icon-search"></el-input>
    <div class="fast-entry">
        <div>
            <icon-button icon="el-icon-date"></icon-button>
            <h5 style="margin: 0">课程</h5>
        </div>
        <div>
            <icon-button icon="el-icon-collection"></icon-button>
            <h5 style="margin: 0">笔记</h5>
        </div>
        <div>
            <icon-button icon="el-icon-star-off"></icon-button>
            <h5 style="margin: 0">收藏</h5>
        </div>
        <div>
            <icon-button icon="el-icon-time"></icon-button>
            <h5 style="margin: 0">历史</h5>
        </div>
    </div>
</div>
</template>

<script>
import IconButton from './IconButton.vue'

export default {
  name: 'main-header',
  components: {IconButton}
}
</script>

<style scoped>
.header-align {
    height: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}

.icon {
    flex-grow: 1;
    margin-left: 10px
}

.section {
    flex-grow: 1;
    margin-right: 10px;
}

.search-bar {
    margin-top: 10px;
    width: 300px;
    height: 50px;
}

.search-bar >>> .el-input__inner {
    background: #f0f0f0;
}

.fast-entry {
    margin-left: 20px;
    flex-grow: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
</style>
