<template id="appointments">
  <app-layout/>
  <div>
    <h2 class="row">Appointment</h2>
    <div class="row"><br /></div>
    <div class="row">
      <div class="col-md-5">
        <div class="row"><br /></div>
        <div class="row row-header">
          <div class="col-md-1">Id</div>
          <div class="col-md-3">Name</div>
          <div class="col-md-5">Email</div>
          <div class="col-md-3">Action</div>
        </div>
        <div class="row row-detail" v-for="user in users" :key="user.id">
          <div class="col-md-1">{{user.id}}</div>
          <div class="col-md-3">{{user.name}}</div>
          <div class="col-md-5">{{user.email}}</div>
          <div class="col-md-3">
            <button rel="tooltip" title="View" @click="fetchDetails(`${user.id}`)">
              <i class="fas fa-eye"  aria-hidden="true"></i></button>
          </div>
        </div>
      </div>
      <div class="col-md-1">
        <br />
      </div>
      <div class="col-md-6">
        <div class="row row-header" v-show="users.length !== 0 && userId!=null"><b>User Id: {{userId}}</b></div>
        <div class="row"><br /></div>
        <div class="card bg-light mb-3"  v-show="users.length !== 0 && userId!=null">
          <div class="card-header">
            Appointment Information
          </div>
          <div class="card-body row" >
            <div class="card-header" v-show="id!=null"><i>Updating Appointment Information with ID {{id}}</i></div>
            <div >
              <div class="col-md-3"><label class="col-form-label">Appointment_type:</label></div>
              <!--              <input v-model="appointment_type" />-->
              <div class="col-md-1">
                <select v-model="appointment_type">
                  <option value="" disabled selected>Select your option</option>
                  <option v-for="category in categories" :value=category>{{ category}}</option>
                </select>
              </div>
              <div class="col-md-3"><label class="col-form-label">Appointment_date: </label></div>
              <div class="col-md-1"><input type="date" v-model.date="appointment_date" /></div>
              <div class="col-md-8"></div>
            </div>
            <div class="row">&nbsp;</div>
            <div class="row">
              <div class="col-md-8"></div>
            </div>

            <div class="col-md-4"></div>
            <div class="col-md-8">
              <button rel="tooltip" title="Save" @click="addDetail()">
                <i class="fas fa-save"  aria-hidden="true"></i></button>
              &nbsp;
              <button v-show="id!=null" rel="tooltip" title="Insert mode" @click="selectInsertMode()">
                <i class="fas fa-add"  aria-hidden="true"></i></button>
            </div>
          </div>
        </div>
        <div class="row"><br /></div>
        <div class="row" v-show="!showDetails">
          <span v-if="userId!=null"> No Appointments for user id {{userId}}. Please add.</span>
          <span v-else>Please click view button for a user.</span>
        </div>
        <div class="row" v-show="showDetails">
          <div class="row row-header2">
            <!-- <div class="col-md-1">Id</div> -->
            <div class="col-md-5">Appointment_type</div>
            <div class="col-md-5">Appointment_date</div>
            <div class="col-md-2">Action</div>
          </div>
          <div class="row row-detail2" v-for="detail in details" :key="detail.id">
            <!-- <div class="col-md-1">{{track.id}}</div> -->
            <div class="col-md-5">{{detail.appointment_type}}</div>
            <div class="col-md-5">{{detail.appointment_date[2]}} {{months[detail.appointment_date[1]-1]}}, {{detail.appointment_date[0]}}</div>
            <div class="col-md-2">
              <button rel="tooltip" title="Edit" @click="updateDetail(detail)">
                <i class="fas fa-pencil"  aria-hidden="true"></i></button> &nbsp;
              <button rel="tooltip" title="Delete" @click="deleteDetail(`${detail.id}`)">
                <i class="fas fa-trash"  aria-hidden="true"></i></button>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
app.component("appointments", {
  template: "#appointments",
  data: () => ({
    users: [],
    details:[],
    showDetails: false,
    userId: null,
    id: null,
    appointment_type: null,
    appointment_date: (new Date()).toISOString().split("T")[0],
    categories: ["nutritionist", "physiotherapist", "gym instructor"],
    months : ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
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
    fetchDetails(userId) {
      axios.get("/api/appointments/" + userId)
          .then(res => {
            this.details = res.data;
            this.showDetails = true;
            this.userId = userId;
          })
          .catch(() => {
            // alert("Error while fetching appointments");
            this.showDetails = false;
            this.userId = userId;
          });
    },
    deleteDetail(id) {
      fetch("/api/appointments/" + id, {
        method: "DELETE",
        cache: "no-store",
      }).then((response) => {
        if (response.status !== 404) {
          alert("The item is deleted");
          this.fetchDetails(this.userId);
        }
      }).catch((err) => {
        console.log("there is an error");
      });
    },
    isInputValid: function () {
      if (this.appointment_type == null || this.appointment_date == null ||
          this.appointment_type === "" || this.appointment_date === "" ) {
        alert("Appointment_type and Appointment_date cannot be black");
        return false;
      }

      if (this.appointment_date < 0 ) {
        alert("Appointment_type and Appointment_date cannot be negative. \n Minimum value 0.0");
        return false;
      }

      return true;
    },
    addDetail() {
      if (!this.isInputValid()) return;
      if(this.id == null) {
        fetch("/api/appointments", {
          method: "POST",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ userId: this.userId, appointment_type: this.appointment_type, appointment_date: this.appointment_date }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the appointment could not be saved.");
        });
      } else {
        fetch("/api/appointments", {
          method: "PUT",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ id:this.id, userId: this.userId, appointment_type: this.appointment_type, appointment_date: this.appointment_date }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the appointment could not be saved.");
        });
      }


    },
    updateDetail: function (detail) {
      this.id = detail.id;
      this.appointment_type = detail.appointment_type;
      date_fields = detail.appointment_date.toString().split(',')
      date_year = date_fields[0];
      date_month = date_fields[1].length == 1? '0'+date_fields[1]: date_fields[1];
      date_day = date_fields[2].length == 1? '0'+date_fields[2]: date_fields[2];
      this.appointment_date = date_year+'-'+date_month+'-'+date_day;
      alert(this.appointment_date)
    },
    selectInsertMode: function () {
      this.appointment_type = null;
      this.appointment_date = null;
      this.id = null;
    }
  }
});
</script>

