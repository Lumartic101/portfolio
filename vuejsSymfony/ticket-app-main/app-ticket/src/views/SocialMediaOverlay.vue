<template>
  <div class="background"/>
  <main class="main">
    <div class="container">
      <div class="header">
        <IconComponent
          :name="'share'"
        />
        <h1>Share</h1>
      </div>
      <div class="social_list">
        <ul>
          <li>
            <SocialMediaCard :heading="'Share by whatsapp'" :name="'whatsapp'" :path="'/src/assets/whatsapp.png'"/>
          </li>
          <li>
            <SocialMediaCard :heading="'Share by facebook'" :name="'facebook'" :path="'/src/assets/facebook.png'"/>
          </li>
          <li>
            <SocialMediaCard :heading="'Share by instagram'" :name="'facebook'" :path="'/src/assets/insta.png'"/>
          </li>
          <li>
            <SocialMediaCard :heading="'Share by telegram'" :name="'facebook'" :path="'/src/assets/telegram.png'"/>
          </li>
        </ul>
      </div>
      <div class="button">
        <BasicButton
          class="basic_button"
          :size="'wide'"
          :label="'Close'"
          :background-color="'#036bd2'"
          @click="$router.push({ name: 'manage' })"
        />
      </div>
    </div>
  </main>
</template>

<script lang="ts" setup>
import IconComponent from '@/components/Atoms/IconComponent.vue';
import SocialMediaCard from '@/components/Molecules/SocialMediaCard.vue';
import BasicButton from '@/components/Atoms/BasicButton.vue';
</script>

<style scoped>
.basic_button {
  width: 264px;
  height: 50px;
}

ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
  align-items: center;
  flex-direction: column;
}

.main {
  padding-top: 10%;
  z-index: 10;
}

.container {
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 348px;
  height: 545px;
  border-radius: 6px;
  background-color: #ffffff;
  box-shadow: 0px 20px 20px rgba(0, 0, 0, 0.02), 0px 10px 10px rgba(0, 0, 0, 0.05), 0px 5px 5px rgba(0, 0, 0, 0.1);
}

.background {
  position: absolute;
  width: 100%;
  height: 976px;
  background-color: #000;
  opacity: 79%;
  z-index: 3;
}

.header {
  padding-top: 27px;
  display: flex;
  align-items: center;
}

.header h1 {
  padding-left: 18px;
  /* Share. */
  font-size: 31px;
  color: #303030;
  text-decoration: none solid rgb(48, 48, 48);
}

.social_list {
  padding-top: 18px;
  width: 264px;
}
</style>
