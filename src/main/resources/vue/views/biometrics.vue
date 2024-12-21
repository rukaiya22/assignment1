<template id="biometrics">
  <app-layout/>
  <div>
    <h2 class="row">Biometric</h2>
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
            Biometric Information
          </div>
          <div class="card-body row" >
            <div class="card-header" v-show="id!=null"><i>Updating Biometric Information with ID {{id}}</i></div>
            <div class="col-md-3"><label class="col-form-label">Bp_systolic:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="bp_systolic" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Bp_diastolic: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="bp_diastolic" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Blood_sugar: </label></div>
            <div class="col-md-1"><input type="number" v-model.number="blood_sugar" /></div>
            <div class="col-md-8"></div>
            <div class="col-md-3"><label class="col-form-label">Cholesterol:</label></div>
            <div class="col-md-1"><input  type="number" v-model.number="cholesterol" /></div>
            <div class="col-md-8"></div>
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
          <span v-if="userId!=null"> No Biometrics for user id {{userId}}. Please add.</span>
          <span v-else>Please click view button for a user.</span>
        </div>
        <div class="row" v-show="showDetails">
          <div class="row row-header2">
            <!-- <div class="col-md-1">Id</div> -->
            <div class="col-md-2">Bp_systolic</div>
            <div class="col-md-2">Bp_diastolic</div>
            <div class="col-md-2">Blood_sugar</div>
            <div class="col-md-3">Cholesterol</div>
            <div class="col-md-3">Action</div>
          </div>
          <div class="row row-detail2" v-for="detail in details" :key="detail.id">
            <!-- <div class="col-md-1">{{track.id}}</div> -->
            <div class="col-md-2">{{detail.bp_systolic}} mmHg</div>
            <div class="col-md-2">{{detail.bp_diastolic}} mmHg</div>
            <div class="col-md-2">{{detail.blood_sugar}} mmol/L</div>
            <div class="col-md-3">{{detail.cholesterol}} mg/dl</div>
            <div class="col-md-3">
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
app.component("biometrics", {
  template: "#biometrics",
  data: () => ({
    users: [],
    details:[],
    showDetails: false,
    userId: null,
    id: null,
    bp_systolic: null,
    bp_diastolic: null,
    blood_sugar: null,
    cholesterol: null,
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
      axios.get("/api/biometrics/" + userId)
          .then(res => {
            this.details = res.data;
            this.showDetails = true;
            this.userId = userId;
          })
          .catch(() => {
            // alert("Error while fetching biometrics");
            this.showDetails = false;
            this.userId = userId;
          });
    },
    deleteDetail(id) {
      fetch("/api/biometrics/" + id, {
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
      if (this.bp_systolic == null || this.bp_diastolic == null || this.blood_sugar == null || this.cholesterol == null ||
          this.bp_systolic === "" || this.bp_diastolic === "" || this.blood_sugar === "" || this.cholesterol === "") {
        alert("Bp_systolic, Bp_diastolic, Blood_sugar and Cholesterol cannot be black");
        return false;
      }

      if (this.bp_systolic < 0 || this.bp_diastolic < 0 || this.blood_sugar < 0 || this.cholesterol < 0) {
        alert("Bp_systolic, Bp_diastolic, Blood_sugar and Cholesterol cannot be negative. \n Minimum value 0.0");
        return false;
      }

      return true;
    },
    addDetail() {
      if (!this.isInputValid()) return;
      if(this.id == null) {
        fetch("/api/biometrics", {
          method: "POST",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ userId: this.userId, bp_systolic: this.bp_systolic, blood_sugar:
            this.blood_sugar, bp_diastolic: this.bp_diastolic, cholesterol:this.cholesterol }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the biometric could not be saved.");
        });
      } else {
        fetch("/api/biometrics", {
          method: "PUT",
          cache: "no-store",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ id:this.id, userId: this.userId, bp_systolic: this.bp_systolic,
            blood_sugar: this.blood_sugar, bp_diastolic: this.bp_diastolic, cholesterol: this.cholesterol }),
        }).then((response) => {
          alert(response.status);
          this.fetchDetails(this.userId);
          this.selectInsertMode();
        }).catch((err) => {
          alert("There is an error, the biometric could not be saved.");
        });
      }


    },
    updateDetail: function (detail) {
      this.id = detail.id;
      this.bp_systolic = detail.bp_systolic;
      this.bp_diastolic = detail.bp_diastolic;
      this.blood_sugar = detail.blood_sugar;
      this.cholesterol = detail.cholesterol
    },
    selectInsertMode: function () {
      this.bp_systolic = null;
      this.bp_diastolic = null;
      this.blood_sugar = null;
      this.cholesterol = null;
      this.id = null;
    }
  }
});
</script>

