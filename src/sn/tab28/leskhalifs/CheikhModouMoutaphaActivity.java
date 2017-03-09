/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sn.tab28.leskhalifs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Demonstrates a "card-flip" animation using custom fragment transactions (
 * {@link android.app.FragmentTransaction#setCustomAnimations(int, int)}).
 * 
 * <p>
 * This sample shows an "info" action bar button that shows the back of a
 * "card", rotating the front of the card out and the back of the card in. The
 * reverse animation is played when the user presses the system Back button or
 * the "photo" action bar button.
 * </p>
 */
public class CheikhModouMoutaphaActivity extends Activity implements
		FragmentManager.OnBackStackChangedListener {
	/**
	 * A handler object, used for deferring UI operations.
	 */
	private Handler mHandler = new Handler();

	/**
	 * Whether or not we're showing the back of the card (otherwise showing the
	 * front).
	 */
	private boolean mShowingBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_flip);

		if (savedInstanceState == null) {
			// If there is no saved instance state, add a fragment representing
			// the
			// front of the card to this activity. If there is saved instance
			// state,
			// this fragment will have already been added to the activity.
			getFragmentManager().beginTransaction()
					.add(R.id.container, new CardFrontFragment()).commit();
		} else {
			mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		}

		// Monitor back stack changes to ensure the action bar shows the
		// appropriate
		// button (either "photo" or "info").
		getFragmentManager().addOnBackStackChangedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// Add either a "photo" or "finish" button to the action bar, depending
		// on which page
		// is currently selected.
		MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
				mShowingBack ? R.string.action_photo : R.string.action_info);
		item.setIcon(mShowingBack ? R.drawable.ic_action_photo
				: R.drawable.ic_action_info);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Navigate "up" the demo structure to the launchpad activity.
			// See http://developer.android.com/design/patterns/navigation.html
			// for more.
			// NavUtils.navigateUpTo(this, new Intent(this,
			// MainActivity.class));
			Intent homeIntent = new Intent(this, MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			return true;

		case R.id.action_flip:
			flipCard();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void flipCard() {
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		// Flip to the back.

		mShowingBack = true;

		// Create and commit a new fragment transaction that adds the fragment
		// for the back of
		// the card, uses custom animations, and is part of the fragment
		// manager's back stack.

		getFragmentManager().beginTransaction()

		// Replace the default fragment animations with animator resources
		// representing
		// rotations when switching to the back of the card, as well as animator
		// resources representing rotations when flipping back to the front
		// (e.g. when
		// the system Back button is pressed).
				.setCustomAnimations(R.animator.card_flip_right_in,
						R.animator.card_flip_right_out,
						R.animator.card_flip_left_in,
						R.animator.card_flip_left_out)

				// Replace any fragments currently in the container view with a
				// fragment
				// representing the next page (indicated by the just-incremented
				// currentPage
				// variable).
				.replace(R.id.container, new CardBackFragment())

				// Add this transaction to the back stack, allowing users to
				// press Back
				// to get to the front of the card.
				.addToBackStack(null)

				// Commit the transaction.
				.commit();

		// Defer an invalidation of the options menu (on modern devices, the
		// action bar). This
		// can't be done immediately because the transaction may not yet be
		// committed. Commits
		// are asynchronous in that they are posted to the main thread's message
		// loop.
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				invalidateOptionsMenu();
			}
		});
	}

	@Override
	public void onBackStackChanged() {
		mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

		// When the back stack changes, invalidate the options menu (action
		// bar).
		invalidateOptionsMenu();
	}

	/**
	 * A fragment representing the front of the card.
	 */
	public static class CardFrontFragment extends Fragment {
		public CardFrontFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_smmoustapha_front,
					container, false);
		}
	}

	/**
	 * A fragment representing the back of the card.
	 */
	public static class CardBackFragment extends Fragment {
		public CardBackFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// -- inflate the layout for this fragment
			View myInflatedView = inflater.inflate(
					R.layout.fragment_smmoustapha_back, container, false);

			// Set the Text to try this out
			TextView title = (TextView) myInflatedView
					.findViewById(R.id.txtTitle);
			TextView content = (TextView) myInflatedView
					.findViewById(R.id.txtContent);
			title.setText(Html
					.fromHtml("<h3>Biographie de  Cheikh Mouhammadou Moustapha MBACKE, premier khalife g&eacute;n&eacute;ral des mourides (1927-1945) </h3>"));
			content.setText(Html
					.fromHtml("<p>Lorsque, de guerre lasse, au terme d&rsquo;un exil p&eacute;nible et  inique en Afrique Centrale, le Pouvoir Colonial se r&eacute;solut &agrave; ramener Cheikh  Ahmadou BAMBA au S&eacute;n&eacute;gal, il se trouva plac&eacute; devant un constat d&rsquo;&eacute;chec quant &agrave;  sa tentative de liquidation du Cheikh et de ses id&eacute;es. Mais il ne d&eacute;sarma pas  pour autant : le combat fut transpos&eacute; sur le plan culturel. Sa nouvelle  strat&eacute;gie fut d&rsquo;entreprendre d&rsquo;effacer de la m&eacute;moire du peuple, jusqu&rsquo;au  souvenir de Cheikh Ahmadou BAMBA par le biais de la scolarisation d&rsquo;enfants dont  on allait planifier savamment le lavage du cerveau, le d&eacute;racinement culturel et  l&rsquo;europ&eacute;anisation par l&rsquo;assimilation aux m&oelig;urs occidentales. Selon les  esp&eacute;rances du Pouvoir Colonial, le Mouridisme devait s&rsquo;effondrer de lui-m&ecirc;me  d&egrave;s la disparition de son fondateur, min&eacute; par les dissensions qui na&icirc;tront  forc&eacute;ment, croit-il, des querelles successorales, mais aussi par les s&eacute;ductions  de la vie mat&eacute;rielle qu&rsquo;il offre. <br />"
							+ "<br />"
							+ " Tous ces espoirs devaient par la suite s&rsquo;&eacute;crouler lamentablement car Cheikh  Ahmadou BAMBA allait laisser une descendance de Vaillants Paladins de l&rsquo;Islam  qui se sont tous illustr&eacute;s dans la d&eacute;fense et la propagation de l&rsquo;&oelig;uvre du  fondateur du Mouridisme. <br />"
							+ " <br />"
							+ " Le premier d&rsquo;entre eux, Cheikh Mouhammadou Moustapha MBACKE se distingue par un  courage incommensurable, une intelligence hors du commun, d&rsquo;immenses qualit&eacute;s  de rassembleur, d&rsquo;organisateur, de b&acirc;tisseur, toutes choses qui ont trouv&eacute; la  pleine mesure de leur expression dans le contexte particuli&egrave;rement dur de  l&rsquo;&eacute;poque coloniale, dans l&rsquo;une de ses p&eacute;riodes les plus tragiques : l&rsquo;entre  deux guerres. <br />"
							+ "<br />"
							+ "  Ce preux Chevalier de l&rsquo;Islam qui allait reprendre et porter haut le flambeau  allum&eacute; par son illustre P&egrave;re, a vu le jour en 1888 &agrave; Darou Salam, d&rsquo;une m&egrave;re  elle-m&ecirc;me issue d&rsquo;une grande famille d&rsquo;&eacute;rudits, Sokhna Aminata L&Ocirc;. C&rsquo;est  d&rsquo;ailleurs son oncle maternel, Serigne Ndame Abdou Rahmane L&Ocirc;, grand compagnon  de son P&egrave;re, qui allait se charger de son initiation au Coran, tandis que Mame  Thierno Birahim MBACKE, fr&egrave;re cadet du Cheikh, allait assurer &agrave; son tour sa  formation dans les questions th&eacute;ologiques. Par la suite, son p&egrave;re, le Cheikh en  personne, se chargera de guider ses pas dans les arcanes de la formation  mystique. Jamais &eacute;tudiant ne fut aussi dou&eacute;. Il excellera &agrave; un point tel que  son p&egrave;re le d&eacute;signera comme successeur avec pour mission, le raffermissement de  la coh&eacute;sion de la Communaut&eacute; Mouride dans le but de la faire prosp&eacute;rer, mais  surtout l&rsquo;&eacute;dification de la Grande Mosqu&eacute;e, pour la seule gloire de Dieu. Il  n&rsquo;est peut-&ecirc;tre pas superflu de dire que les contemporains ont rapport&eacute; que son  P&egrave;re lui t&eacute;moignait une r&eacute;elle affection car on avait le sentiment qu&rsquo;il savait  qu&rsquo;il avait bien investi sa confiance. <br />"
							+ " <br />"
							+ "  La premi&egrave;re occasion que Cheikh Mouhammadou Moustapha MBACKE eut de montrer  qu&rsquo;il &eacute;tait &agrave; la hauteur des esp&eacute;rances de son P&egrave;re, ce fut en 1927, lorsque le  Cheikh disparut. La rapidit&eacute; et la pertinence de sa r&eacute;action, le sang froid, la  discr&eacute;tion et le courage avec lesquels il fit transf&eacute;rer l&rsquo;illustre corps &agrave;  Touba, dans le contexte tr&egrave;s coercitif de la p&eacute;riode coloniale forcent encore,  de nos jours, l&rsquo;admiration, quand on sait qu&rsquo;il n&rsquo;&eacute;tait pas facile &agrave; l&rsquo;&eacute;poque  de braver le Pouvoir Blanc (il a donn&eacute; une s&eacute;pulture &agrave; son p&egrave;re sans informer  l&rsquo;Administration, en se passant surtout de son autorisation) et d&rsquo;encourir les  foudres de son courroux. Au m&eacute;pris des risques patents, il a ex&eacute;cut&eacute; les  derni&egrave;res volont&eacute;s de son p&egrave;re : lui assurer une s&eacute;pulture en tout conforme &agrave;  ses v&oelig;ux, selon la proc&eacute;dure qu&rsquo;il avait lui-m&ecirc;me indiqu&eacute;e, surtout en  s&rsquo;assurant que son corps ne soit point souill&eacute;, ne serait- ce que par le simple  regard d&rsquo;un membre de l&rsquo;administration coloniale. <br />"
							+ "  <br />"
							+ "  Une autre manifestation de sa pleine capacit&eacute; &agrave; jouer le r&ocirc;le que son p&egrave;re lui  a d&eacute;volu a &eacute;t&eacute; la mani&egrave;re dont il a mis fin aux vell&eacute;it&eacute;s de dissidence de  certains Grands Cheikhs apr&egrave;s la disparition du Fondateur. Par son aura  personnelle, et ses qualit&eacute;s de grand rassembleur, il a r&eacute;ussi &agrave; rallier autour  de sa personne tous les dignitaires et les talib&eacute;s. Pour assurer la coh&eacute;sion et  la force de la Communaut&eacute;, il a, avec intelligence, choisi la voie du dialogue  et de la concertation. <br />"
							+ "  <br />"
							+ "  D&rsquo;abord avec ses fr&egrave;res et s&oelig;urs : bien qu&rsquo;il f&ucirc;t l&rsquo;a&icirc;n&eacute; et le l&eacute;gataire de  Serigne TOUBA, donc le seul ma&icirc;tre, autoris&eacute; &agrave; d&eacute;cider souverainement avec  l&rsquo;assurance d&rsquo;obtenir l&rsquo;ob&eacute;issance stricte de ses cadets qui voyaient en lui  leur v&eacute;n&eacute;r&eacute; P&egrave;re, il a pr&eacute;f&eacute;r&eacute;, en toute chose, les consulter pour tenir  compte, tr&egrave;s &eacute;troitement, de leurs avis. D&rsquo;ailleurs, il est de notori&eacute;t&eacute;  publique qu&rsquo;il v&eacute;n&eacute;rait ses fr&egrave;res et s&oelig;urs qu&rsquo;au demeurant il ch&eacute;rissait, car  lui aussi voyait en chacun d&rsquo;entre eux son illustre P&egrave;re. Sur cette question de  ses rapports avec ses cadets, le t&eacute;moignage de Sokhna Ma&iuml;mouna MBACKE la  benjamine du Cheikh est particuli&egrave;rement &eacute;difiant. En effet elle aimait souvent  raconter que, toute jeune, encore du vivant de leur v&eacute;n&eacute;r&eacute; p&egrave;re, &agrave; un &acirc;ge o&ugrave;  elle n&rsquo;avait pas encore une conscience claire de son lien de parent&eacute; avec  Cheikh Mouhammadou Moustapha, son attention avait &eacute;t&eacute; attir&eacute;e par  l&rsquo;empressement de ce jeune homme &agrave; aller au devant de ses moindres d&eacute;sirs, &agrave;  elle et aux autres enfants du Cheikh. <br />"
							+ "  <br />"
							+ " Elle avait remarqu&eacute; chez lui un z&egrave;le et un d&eacute;vouement qui allait m&ecirc;me, souvent,  jusqu&rsquo;&agrave; leur offrir son v&ecirc;tement pour s&rsquo;essuyer les mains apr&egrave;s les repas. Elle  avait fini alors par dire &agrave; ses fr&egrave;res : &quot; Qu&rsquo;il est bon, ce talib&eacute; de  notre p&egrave;re ! &quot; <br />"
							+ "  <br />"
							+ "  Ensuite avec les Cheikhs et autres Dignitaires du Mouridisme : &agrave; l&rsquo;exemple de  son P&egrave;re, il a t&eacute;moign&eacute; une grande consid&eacute;ration, un grand respect aux Cheikhs  et &agrave; tous les Dignitaires. Il n&rsquo;a jamais manqu&eacute; de prendre leurs conseils  chaque fois qu&rsquo;il s&rsquo;est agi des grandes questions int&eacute;ressant le devenir de la  Communaut&eacute;. Il leur a conf&eacute;r&eacute; certaines pr&eacute;rogatives destin&eacute;es &agrave; accro&icirc;tre et &agrave;  fortifier cette Communaut&eacute;. A l&rsquo;instar de son P&egrave;re, il a cr&eacute;e pour eux des  Daaras, v&eacute;ritables p&ocirc;les de d&eacute;veloppement o&ugrave;, en dehors de l&rsquo;enseignement du  Coran et de la liturgie, le travail productif est &eacute;rig&eacute; au rang de v&eacute;ritable  sacerdoce. C&rsquo;est ainsi que, pour doter les Cheikhs, il eut &agrave; fonder de nombreux  villages dont on peut, pour m&eacute;moire, citer quelques- uns des plus connus :  Tindody, Ta&iuml;f , Na&iuml;d&eacute;, Darou Na&iuml;m, Ka&eacute;l, Bayla. Il est peut-&ecirc;tre utile de  rappeler que Ta&iuml;f et Bayla ont la particularit&eacute; que leur production &eacute;tait  exclusivement consacr&eacute;e au financement des grands chantiers que sont la Grande  Mosqu&eacute;e et le rail Diourbel - Touba. A ce titre, ces daaras pr&eacute;figurent le  Khelcome de Serigne Saliou qui n&rsquo;a pour objectif, en ce qui concerne les  revenus qu&rsquo;il g&eacute;n&egrave;re, que le financement des travaux de Serigne Touba. <br />"
							+ "  <br />"
							+ "  Enfin avec le reste de la Ummah : toute sa vie durant, il s&rsquo;est &eacute;vertu&eacute; &agrave;  tisser des liens &eacute;troits de fraternit&eacute; et de collaboration avec les autres  chefs religieux, non seulement du S&eacute;n&eacute;gal mais aussi des pays limitrophes comme  la Mauritanie. Il est connu que Seydou Nourou TALL, repr&eacute;sentant de la famille  omarienne lui rendait souvent visite et qu&rsquo;il eut &agrave; recevoir &agrave; Touba le Roi du  TRARZA venu de sa Mauritanie natale pour rendre visite &agrave; son fr&egrave;re en Islam. <br />"
							+ "  <br />"
							+ "  M&ecirc;me avec l&rsquo;Administration Coloniale, il a r&eacute;ussi &agrave; &eacute;tablir de bons rapports &agrave;  un point tel, que le Gouverneur G&eacute;n&eacute;ral de L&rsquo;A.O.F en personne a &eacute;t&eacute; son h&ocirc;te &agrave;  TOUBA, trois jours durant. <br />"
							+ "  <br />"
							+ "  La plus grande r&eacute;ussite &agrave; mettre &agrave; l&rsquo;actif de Cheikh Mouhammadou Moustapha est,  sans conteste, la construction de la Grande Mosqu&eacute;e de TOUBA. <br />"
							+ "  <br />"
							+ "  C&rsquo;&eacute;tait un projet tellement cher &agrave; Cheikh Ahmadou BAMBA qu&rsquo;il en dira lui-m&ecirc;me,  bien avant sa construction, &quot;L&rsquo;Eternel m&rsquo;a honor&eacute; pour l&rsquo;&eacute;ternit&eacute; d&rsquo;un  &eacute;difice indestructible qui se dressera jusqu&rsquo;au Paradis. &quot; <br />"
							+ "  <br />"
							+ "  A l&rsquo;endroit de ceux qui, de pr&egrave;s ou de loin ont eu le bonheur de collaborer ou  de participer &agrave; l&rsquo;&eacute;rection de l&rsquo;ouvrage, le Cheikh a formul&eacute; les pri&egrave;res  suivantes : <br />"
							+ "  <br />"
							+ "&quot; Absous les volontaires qui ont b&acirc;ti l&rsquo;&eacute;difice si &eacute;lev&eacute; de ma demeure, la  Cit&eacute; B&eacute;nite de TOUBA, de leurs p&ecirc;ch&eacute;s du pass&eacute; et de l&rsquo;avenir ; absous tous  ceux qui avaient la charge de l&rsquo;ordonnancement des travaux de l&rsquo;&eacute;difice de  leurs p&ecirc;ch&eacute;s initiaux et finaux. &quot; <br />"
							+ "   <br />"
							+ "&quot; Absous &eacute;galement tous ceux qui leur sont venus en aide dans cet &eacute;difice  qui, par Ta Gloire s&rsquo;est &eacute;rig&eacute; - &Ocirc; ! Combien Majestueux - de leurs p&ecirc;ch&eacute;s  d&rsquo;avant et d&rsquo;apr&egrave;s. &quot; <br />"
							+ "   <br />"
							+ "  Il convient de rappeler que Cheikhoul Khadim n&rsquo;avait assign&eacute; aux hommes la  mission de construire la Mosqu&eacute;e que dans la noble intention de leur ouvrir les  voies de la R&eacute;demption. Cette Mosqu&eacute;e est un dessein de Dieu et le Cheikh, dans  son exhortation aux talib&eacute;s &agrave; s&rsquo;impliquer dans sa construction, n&rsquo;a pas manqu&eacute;  de pr&eacute;venir : <br />"
							+ "  <br />"
							+ "&quot; Si vous l&rsquo;entreprenez, Dieu en sera pour autant glorifi&eacute; mais en cas de  renonciation, Dieu enverrait des &ecirc;tres pour s&rsquo;en acquitter. &quot; <br />"
							+ "   <br />"
							+ "  Le moment venu, Cheikh Mouhammadou Moustapha entreprit de s&rsquo;atteler &agrave; la  r&eacute;alisation du v&oelig;u de son P&egrave;re. Alors, devant lui, se dress&egrave;rent nombres  d&rsquo;obstacles et d&rsquo;emb&ucirc;ches tous plus ardus les uns que les autres. Mais,  courageusement, opini&acirc;trement, avec d&eacute;termination, il a r&eacute;ussi &agrave; les abattre  les uns apr&egrave;s les autres. <br />"
							+ " <br />"
							+ " Lorsque, le vendredi 17 dhul - qi da 1530 H (4 Mars 1932), il proc&eacute;dait &agrave; la  pose de la premi&egrave;re pierre de l&rsquo;&eacute;difice en pr&eacute;sence des Dignitaires du  Mouridisme et d&rsquo;une foule de Talib&eacute;s enthousiastes, que d&rsquo;obstacles il avait du  abattre pour en arriver &agrave; ce jour et &agrave; ses fastes. <br />"
							+ " <br />"
							+ " Il a du batailler ferme pour obtenir l&rsquo;immatriculation du terrain devant porter  l&rsquo;ouvrage et l&rsquo;autorisation de construire. <br />"
							+ " <br />"
							+ "  Ensuite l&rsquo;Autorit&eacute; Coloniale lui a impos&eacute; une condition qui, dans sa logique  devait signifier le coup d&rsquo;arr&ecirc;t mettant d&eacute;finitivement fin au projet. Il ne  s&rsquo;agissait, ni plus ni moins, que de poser 50 km de chemin de fer, de Diourbel  &agrave; Touba pour acheminer le mat&eacute;riel lourd n&eacute;cessaire &agrave; l&rsquo;entreprise. Dans les  normes, seuls un gouvernement ou une soci&eacute;t&eacute; puissante pouvaient relever un  pareil d&eacute;fi. C&rsquo;&eacute;tait compter sans la d&eacute;termination de Cheikh Mouhammadou  Moustapha : dans un d&eacute;lai de loin inf&eacute;rieur &agrave; celui imparti par le Pouvoir  Colonial et avec les seules ressources (humaines et financi&egrave;res) de la  Communaut&eacute; Mouride, l&rsquo;ouvrage fut r&eacute;alis&eacute;. <br />"
							+ " <br />"
							+ "  Enfin, il a eu &agrave; d&eacute;jouer les man&oelig;uvres frauduleuses d&rsquo;un certain Pierre  TAIILERIE, Administrateur Colonial ayant rev&ecirc;tu le manteau d&rsquo;entrepreneur pour  se faire adjuger le contrat de construction de la Grande Mosqu&eacute;e. Tr&egrave;s vite, il  est apparu qu&rsquo;on avait affaire avec un escroc qui croyait pouvoir s&rsquo;enrichir  sans risque en misant sur l&rsquo;ignorance du droit de ses victimes et surtout sur  la peur qu&rsquo;elle devrait normalement avoir de tra&icirc;ner un blanc devant les  juridictions, aussi bien coloniales que m&eacute;tropolitaines. <br />"
							+ " <br />"
							+ "  Par sa d&eacute;termination Cheikh Mouhammadou Moustapha obtint la condamnation de  TAIILERIE. Les travaux de la Grande Mosqu&eacute;e reprirent de plus belle et les  Talib&eacute;s continu&egrave;rent de rivaliser d&rsquo;ardeur et de sacrifice pour la r&eacute;ussite de  l&rsquo;entreprise. <br />"
							+ "  <br />"
							+ "  <br />"
							+ "  Le Vendredi 7 Juin 1963, jour de l&rsquo;inauguration de la Grande Mosqu&eacute;e par Cheikh  Mouhammadou Falilou le digne successeur de Cheikh Mouhammadou Moustapha, tous  les c&oelig;urs, &agrave; l&rsquo;unanimit&eacute;, se sont souvenus, avec &eacute;motion, du premier Khalife de  Khadimou Rassoul, disparu le 13 Juillet 1945, alors que l&rsquo;&eacute;difice avait d&eacute;j&agrave;  pris forme : les fondations en &eacute;taient achev&eacute;es et les murs avaient atteint la  hauteur d&rsquo;une terrasse. L&rsquo;image d&rsquo;un travailleur infatigable, d&rsquo;un &eacute;rudit  poss&eacute;dant &agrave; la perfection les Sciences Coraniques et la langue arabe planait  sur l&rsquo;assistance. <br />"
							+ "  <br />"
							+ "  <br />"
							+ "  L&rsquo;on gardait encore en m&eacute;moire la c&eacute;l&eacute;bration du Premier Magal apr&egrave;s Serigne  Touba d&egrave;s 1928 (dans le sillage du Fondateur qui l&rsquo;organisait lui-m&ecirc;me), point  de d&eacute;part d&rsquo;une tradition solidement &eacute;tablie de nos jours et qui est devenu  l&rsquo;un des &eacute;v&eacute;nements les plus importants du monde musulman. <br />"
							+ "  <br />"
							+ " O&ugrave; qu&rsquo;on puisse poser le regard, aussi loin que porte la vue, tout &eacute;voque la  puissante stature de Cheikh Mouhammadou Moustapha : c&rsquo;est lui qui a fait de  TOUBA la m&eacute;tropole religieuse, la ville sainte, la capitale du Mouridisme  qu&rsquo;elle est devenue et qui lui doit son premier forage qu&rsquo;il fit installer &agrave;  NDAME. C&rsquo;est lui qui a cr&eacute;e chez les Mourides ce go&ucirc;t prononc&eacute; du travail,  cette d&eacute;termination &agrave; vivre honn&ecirc;tement du fruit de son labeur et cette volont&eacute;  de vivre en parfaite conformit&eacute; avec les enseignements du Cheikh. Ce n&rsquo;est pas  hasard si, sous son impulsion, le Baol est devenu le principal producteur  d&rsquo;arachide. Lui-m&ecirc;me a eu &agrave; &ecirc;tre d&eacute;cor&eacute; de la M&eacute;daille du M&eacute;rite Agricole. <br />"
							+ " <br />"
							+ " Malgr&eacute; la Crise des ann&eacute;es 1930 et les effets n&eacute;gatifs de la Seconde Guerre  Mondiale sur l&rsquo;&eacute;conomie en g&eacute;n&eacute;ral, le terroir mouride est demeur&eacute; riche,  prosp&egrave;re, irr&eacute;m&eacute;diablement inscrit dans une logique de travail, de discipline  et de ferveur religieuse, gr&acirc;ce &agrave; l&rsquo;enseignement de Serigne Touba relay&eacute; par  Cheikh Mouhammadou Moustapha. On se souvient que c&rsquo;est lui que le Cheikh avait  d&eacute;sign&eacute; pour remettre &agrave; l&rsquo;Administration Coloniale la somme de 500 000 francs  dans le but d&rsquo;aider &agrave; relever la monnaie fran&ccedil;aise menac&eacute;e d&rsquo;effondrement. Quel  bel exemple de sagesse, de d&eacute;passement et de g&eacute;n&eacute;rosit&eacute; &agrave; l&rsquo;endroit d&rsquo;un  syst&egrave;me qui pourtant, &agrave; l&rsquo;&eacute;gal d&rsquo;un ennemi d&eacute;termin&eacute;, s&rsquo;est toujours &eacute;vertu&eacute; &agrave;  nuire ou &agrave; porter pr&eacute;judice &agrave; la Communaut&eacute; et &agrave; son Guide. <br />"
							+ " <br />"
							+ " L&rsquo;on ne peut regarder le rail &agrave; Touba, l&rsquo;on ne peut se recueillir dans la  Sainte Mosqu&eacute;e, l&rsquo;on ne peut traverser Darou Khoudosse le c&oelig;ur de Touba sans  &eacute;voquer cette grande figure de l&rsquo;Islam Universel dont les jeunes g&eacute;n&eacute;rations ne  connaissent &agrave; travers les photographies, qu&rsquo;un visage empreint de bont&eacute; et de  s&eacute;r&eacute;nit&eacute; et tout baign&eacute; de la lumi&egrave;re de Serigne Touba &agrave; la t&ecirc;te envelopp&eacute; d&rsquo;un  turban, toutes choses qui corroborent les t&eacute;moignages de ses contemporains le  d&eacute;crivant comme un travailleur infatigable, r&eacute;solument d&eacute;tourn&eacute; des mondanit&eacute;s,  uniquement pr&eacute;occup&eacute; des pr&eacute;ceptes de l&rsquo;Islam et enti&egrave;rement d&eacute;vou&eacute; &agrave; la m&eacute;moire  de son P&egrave;re. Il pilotait personnellement les travaux de la Grande Mosqu&eacute;e et  n&rsquo;h&eacute;sitait pas, &agrave; l&rsquo;occasion, &agrave; mettre la main &agrave; la p&acirc;te. <br />"
							+ " <br />"
							+ " C&rsquo;est cet homme r&eacute;put&eacute; pour son &eacute;quit&eacute;, son sens de l&rsquo;humain et qui ne faisait  pas de diff&eacute;rence entre le puissant et le pauvre que les talib&eacute;s &eacute;voquent  encore aujourd&rsquo;hui en le d&eacute;signant affectueusement et nostalgiquement sous les  surnoms de Amdy ou de Ndiagne pour faire allusion &agrave; son abondante chevelure. <br />"
							+ "  <br />"
							+ "  Nul ne doute que son &oelig;uvre est agr&eacute;&eacute;e et que son P&egrave;re est satisfait de lui,  tout autant que sa sainte descendance et ses vaillants fr&egrave;res qui, apr&egrave;s lui,  sur son exemple, ont port&eacute; haut le flambeau transmis par le FONDATEUR. <br />"
							+ "  <br />"
							+ "  Source HTCOM.SN (http://www.htcom.sn)</p>"));

			return myInflatedView;
		}
	}
}
