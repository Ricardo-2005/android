<<<<<<< HEAD
# android
安卓开发
=======
# Health Food Monitoring System (Android Native)

一款 **Android 原生健康管理应用**，集成账号登录、个人档案、**AI 饮食热量识别**、每日摄入统计以及健身训练视频与打卡功能。  
应用支持用户通过**自然语言描述饮食内容**，系统调用 **DeepSeek API** 解析食物并估算热量，结果存储在本地数据库（Room），实现低门槛、低成本的健康管理体验。

> ⚠️ 提示：所有热量数据均为 AI 估算值，仅供参考。

---

## ✨ 功能特性

### 1. 账号系统
- 本地注册 / 登录
- 首次启动必须登录
- 支持退出登录
- 一个账号绑定一份个人档案（1:1）

### 2. 个人档案（Profile）
- 身高、体重、年龄、性别等基础信息
- 用于 BMI 与推荐热量计算
- 支持信息编辑与更新

### 3. 饮食监控（核心功能）
- 支持自然语言输入饮食描述  
  示例：`中午吃了两碗米饭，一个鸡腿，一杯牛奶`
- 调用 DeepSeek AI 进行解析
- 自动识别：
  - 食物名称
  - 估算重量（g）
  - 估算热量（kcal）
- 解析结果写入本地 Room 数据库

### 4. 每日热量统计
- 首页与饮食页展示当日总热量
- 按日期聚合饮食数据
- 使用 MPAndroidChart 展示摄入趋势

### 5. 健身训练与打卡
- 训练分类与训练动作列表
- 训练详情页支持：
  - 视频播放（ExoPlayer）
  - 圆环倒计时
  - 训练完成打卡
- 训练日志本地存储

---

## 🧱 技术栈

- **语言**：Java  
- **架构**：MVVM（ViewModel + LiveData）  
- **本地数据库**：Room（SQLite）  
- **UI**：Material Components、RecyclerView  
- **图表**：MPAndroidChart  
- **视频播放**：ExoPlayer  
- **图片加载**：Glide  
- **网络请求**：OkHttp  
- **日期选择**：MaterialDatePicker  

---

## 🏗️ 架构设计（MVVM）


- View 负责 UI 展示
- ViewModel 管理界面状态
- Repository 统一数据访问入口
- Room 负责本地数据持久化

---

## 🗃️ 数据库设计（Room）

数据库名称：`foodcalu.db`

### 主要表
- `user_account`：账号信息  
- `user_profile`：个人档案（与账号 1:1）  
- `diet_record`：饮食记录（AI 解析结果）  
- `workout_category`：训练分类  
- `workout_item`：训练动作  
- `workout_log`：训练日志  

### diet_record 表字段说明

| 字段 | 说明 |
|---|---|
| id | 主键 |
| date | 归一化日期 |
| raw_input | 用户原始输入 |
| food_name | AI 识别的食物名称 |
| estimated_weight_g | 估算重量（g） |
| estimated_calories | 估算热量（kcal） |

---

## 🤖 DeepSeek AI 输出规范

系统使用固定 System Prompt，强制模型只返回 JSON：

```json
{
  "items": [
    {
      "food": "food name",
      "estimated_weight_g": 120,
      "calories_kcal": 180
    }
  ],
  "total_calories_kcal": 180,
  "note": "Calories are AI estimates for reference only"
}

⚙️ 开发环境

Android Studio

MinSdk：23

TargetSdk：34

JDK：11

🚀 运行方式
1. 克隆项目
git clone <your_repo_url>

2. 配置 DeepSeek Key

在项目根目录的 local.properties 中添加：

DEEPSEEK_API_KEY=your_api_key
DEEPSEEK_BASE_URL=https://api.deepseek.com


⚠️ local.properties 不应提交到 GitHub。

3. 运行项目

Sync Gradle

选择设备或模拟器运行

🧪 测试说明

登录 / 注册流程测试

多种自然语言饮食输入测试

AI 返回异常处理测试

Room 数据持久化与聚合统计验证

训练计时与打卡流程测试

UI 空状态与错误提示检查

📸 Screenshots

建议至少包含以下页面截图：

登录页面

首页（BMI + 推荐摄入 + 今日摄入）

饮食输入弹窗

AI 解析后的饮食记录列表

训练详情页（视频 + 圆环计时）

个人档案页面

📌 项目总结

本项目实现了一个 基于 AI 的 Android 健康管理应用，通过自然语言输入显著降低饮食记录门槛，并结合 MVVM 架构与 Room 本地数据库，保证系统的稳定性与可维护性。
项目无需后端即可运行，具备良好的扩展潜力，适合作为课程设计或个人 Android 项目展示。
>>>>>>> 6748bc9 (init: Android Health Food Monitoring System)
