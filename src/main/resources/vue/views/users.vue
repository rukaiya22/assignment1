<template id="users">
  <app-layout/>
  <div>
   <span class="row">
    Name: <input v-model="name" /> Email:
    <input v-model="email" />
    <button @click="addUser">Add new users</button>
   </span>
  </div>
<!--  <h2>All users</h2>-->
  <div id="users">
    <div class="row"><br /></div>
    <div class="row bg-primary">
      <div class="col-md-1">Id</div>
      <div class="col-md-3">Name</div>
      <div class="col-md-5">Email</div>
      <div class="col-md-3">Action</div>
    </div>
    <div class="row bg-success" v-for="user in users" :key="user.id">
      <div class="col-md-1">{{user.id}}</div>
      <div class="col-md-3">{{user.name}}</div>
      <div class="col-md-5">{{user.email}}</div>
      <div class="col-md-3">
<!--        <button @click="viewUser(`${user.id}`)">View</button>-->
        <button @click="deleteUser(`${user.id}`)">Delete</button>
      </div>
    </div>
  </div>
</template>
<script>
app.component("users", {
  template: "#users",
  data: () => ({
    users: [],
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    addUser() {
      if (this.name === "") {
        alert("Name cannot be black");
        return;
      }
      if (this.email === "") {
        alert("Email cannot be black");
        return;
      }
      fetch("/api/users", {
        method: "POST",
        cache: "no-store",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name: this.name, email: this.email }),
      })
          .then((response) => {
            alert(response.status);
            this.fetchUsers();
          })
          .catch((err) => {
            alert("There is an error, the user could not be saved.");
          });
    }
  }
});
</script>

<style scoped>
.bg-success{
  color: yellow;
}

.bg-primary{
  color: white;
}

</style>