<template id="user-activity-overview">
  <app-layout/>
  <div>
    <h3>Activities list </h3>
    <ul>
      <li v-for="activity in activities">
        Total calories: {{activity.calories}} cal Total walk: {{activity.walkHours}} km and Drinking {{activity.drinking}} Liter
      </li>
    </ul>
  </div>
</template>

<script>
app.component("user-activity-overview",{
  template: "#user-activity-overview",
  data: () => ({
    activities: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/trackers/${userId}/`)
        .then(res => this.activities = res.data)
        .catch(() => alert("Error while fetching activities"));
  }
});
</script>

<style scoped>

</style>