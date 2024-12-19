<template id="users">
  <app-layout/>
  <div class="card bg-light mb-3">
    <div class="card-header">
      User Profile
    </div>
    <div class="card-body">
      <label class="col-form-label">User Name: </label>
      <input  class="form-control" v-model="name" />
      <label class="col-form-label">Email: </label>
      <input  class="form-control" v-model="email" />
      <br/>
      <button @click="addUser">Add new users</button>
    </div>
  </div>
<!--  <h2>All users</h2>-->
  <div>
    <div class="row"><br /></div>
    <div class="row row-header col-form-label">
      <div class="col-md-1">Id</div>
      <div class="col-md-3">Name</div>
      <div class="col-md-5">Email</div>
      <div class="col-md-3">Action</div>
    </div>
    <div class="row row-detail col-form-label" v-for="user in users" :key="user.id">
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
      if (this.name === "" || this.name == null) {
        alert("Name cannot be black");
        return;
      }
      if (this.email === "" || this.email == null) {
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
    },
    deleteUser(id) {
      fetch("/api/users/" + id, {
        method: "DELETE",
        cache: "no-store",
      }).then((response) => {
        if (response.status !== 404) {
          alert("The item is deleted");
          this.fetchUsers();
        }
      }).catch((err) => {
        console.log("there is an error");
      });
    }
  }
});
</script>

<style scoped>

</style>