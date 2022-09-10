import React from 'react'
import { ReactSession } from 'react-client-session';



function Profile() {
  const user=ReactSession.get("user");
  console.log("Mu User");
  console.log(user);
  return (
    <div id='ride'>
      <div className="card">
        <div className="card-body">
         <img src={user.photo} />  
          <h5 className="card-title">Name : {user.first_name} {user.last_name}</h5>
        </div>
        <ul className="list-group list-group-flush">
          <li className="list-group-item">Gender :{user.gender}</li>
          <li className="list-group-item">Email : {user.email_id}</li>
          <li className="list-group-item">Contact No : {user.contact}</li>
        </ul>
        <div className="card-body">
          <a href="/UpdateProfile" className="card-link">update Profile</a>
          <a href="/MyRides" className="card-link">Show all Rides Published by me</a>
          <a href="/MyBookings" className="card-link">Show all Bookings done by me</a>
        </div>
      </div>

    </div>
  )
}

export default Profile