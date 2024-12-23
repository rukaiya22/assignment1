<template id="users">
  <app-layout/>
  <div class="card bg-light mb-3">
    <div class="card-header">
      User Profile
    </div>
    <div class="card-body">
      <div class="card-header" v-show="id!=null"><i>Updating profile with ID {{id}}</i></div>
      <label class="col-form-label">User Name: </label>
      <input  class="form-control" v-model="name" />
      <label class="col-form-label">Email: </label>
      <input  class="form-control" v-model="email" />
      <br/>
      <button rel="tooltip" title="Save" @click="addUser">
        <i class="fas fa-save"  aria-hidden="true"></i></button>&nbsp;
      <button v-show="id!=null" rel="tooltip" title="Insert mode" @click="selectInsertMode()">
        <i class="fas fa-add"  aria-hidden="true"></i></button>
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
        <button rel="tooltip" title="Edit" @click="updateUser(user)">
          <i class="fas fa-pencil"  aria-hidden="true"></i></button> &nbsp;
        <button rel="tooltip" title="Delete" @click="deleteUser(`${user.id}`)">
          <i class="fas fa-trash"  aria-hidden="true"></i></button>
      </div>
    </div>
  </div>
</template>
<script>
app.component("users", {
  template: "#users",
  data: () => ({
    users: [],
    id: null,
    name: null,
    email: null
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
    isInputValid: function () {
      if (this.name === "" || this.name == null) {
        alert("Name cannot be black");
        return false;
      }
      if (this.email === "" || this.email == null) {
        alert("Email cannot be black");
        return false;
      }
      const regex_valid_email = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if(!regex_valid_email.test(this.email)) {
        alert("Please correct the email format");
        return false;
      }
      return true;
    },
    addUser: function () {
      if (!this.isInputValid()) return;
      if(this.id ==null){
        fetch("/api/users", {
          method: "POST",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ name: this.name, email: this.email }),
        }).then((response) => {
          alert(response.status);
          this.fetchUsers();
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the user could not be saved.");
        });
      } else {
        fetch("/api/users", {
          method: "PUT",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ id: this.id, name: this.name, email: this.email }),
        }).then((response) => {
          this.id = null;
          alert(response.status);
          this.fetchUsers();
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the user could not be saved.");
        });
      }

    },
    deleteUser: function (id) {
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
    },
    updateUser: function (user) {
      this.id = user.id;
      this.name = user.name;
      this.email = user.email;
    },
    selectInsertMode: function () {
      this.id = null;
      this.name = null;
      this.email = null;
    }
  }
});
</script>

<style scoped>

</style>