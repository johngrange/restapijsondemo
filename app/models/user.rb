class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :encryptable, :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable, :omniauthable

  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :password_confirmation, :remember_me, :name 
  attr_accessible :first_name, :last_name
	def self.find_for_google_oauth2(access_token, signed_in_resource=nil)
		data = access_token.info
		user = User.where(:email => data["email"]).first

		unless user
			user = User.create(name: data["name"],
					   email: data["email"],
					   password: Devise.friendly_token[0,20]
					  )
		end
		user
	end  
end
