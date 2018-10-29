/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * 
 */
package com.execue.nlp.service.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nihar
 */
public class VerbFormServiceImpl {

   private static Map<String, String> presentToSimplePastVerbMAP;
   private static Map<String, String> presentToPastParticipleVerbMAP;
   private static Map<String, String> simplePastToPresentVerbMAP;

   public void loadVerbForms () {
      // load prsentToSimplePastVerbMAP
      presentToSimplePastVerbMAP = new HashMap<String, String>();
      presentToSimplePastVerbMAP.put("arise", "arose");
      presentToSimplePastVerbMAP.put("awake", "awoke");
      presentToSimplePastVerbMAP.put("am", "was");
      presentToSimplePastVerbMAP.put("bear", "bore");
      presentToSimplePastVerbMAP.put("beat", "beat");
      presentToSimplePastVerbMAP.put("become", "became");
      presentToSimplePastVerbMAP.put("begin", "began");
      presentToSimplePastVerbMAP.put("bend", "bent");
      presentToSimplePastVerbMAP.put("bet", "bet");
      presentToSimplePastVerbMAP.put("bind", "bound");
      presentToSimplePastVerbMAP.put("bite", "bit");
      presentToSimplePastVerbMAP.put("bleed", "bled");
      presentToSimplePastVerbMAP.put("blow", "blew");
      presentToSimplePastVerbMAP.put("breed", "bred");
      presentToSimplePastVerbMAP.put("bring", "brought");
      presentToSimplePastVerbMAP.put("build", "built");
      presentToSimplePastVerbMAP.put("burn", "burned");
      presentToSimplePastVerbMAP.put("buy", "bought");
      presentToSimplePastVerbMAP.put("cast", "cast");
      presentToSimplePastVerbMAP.put("catch", "caught");
      presentToSimplePastVerbMAP.put("choose", "chose");
      presentToSimplePastVerbMAP.put("cling", "clung");
      presentToSimplePastVerbMAP.put("come", "came");
      presentToSimplePastVerbMAP.put("cost", "cost");
      presentToSimplePastVerbMAP.put("creep", "crept");
      presentToSimplePastVerbMAP.put("cut", "cut");
      presentToSimplePastVerbMAP.put("deal", "dealt");
      presentToSimplePastVerbMAP.put("dig", "dug");
      presentToSimplePastVerbMAP.put("dive", "dived");
      presentToSimplePastVerbMAP.put("do", "did");
      presentToSimplePastVerbMAP.put("drag", "dragged");
      presentToSimplePastVerbMAP.put("draw", "drew");
      presentToSimplePastVerbMAP.put("dream", "dreamed");
      presentToSimplePastVerbMAP.put("drink", "drank");
      presentToSimplePastVerbMAP.put("drive", "drove");
      presentToSimplePastVerbMAP.put("drown", "drowned");
      presentToSimplePastVerbMAP.put("eat", "ate");
      presentToSimplePastVerbMAP.put("fall", "fell");
      presentToSimplePastVerbMAP.put("feed", "fed");
      presentToSimplePastVerbMAP.put("feel", "felt");
      presentToSimplePastVerbMAP.put("fight", "fought");
      presentToSimplePastVerbMAP.put("find", "found");
      presentToSimplePastVerbMAP.put("fit", "fit");
      presentToSimplePastVerbMAP.put("flee", "fled");
      presentToSimplePastVerbMAP.put("fling", "flung");
      presentToSimplePastVerbMAP.put("fly", "flew");
      presentToSimplePastVerbMAP.put("forbid", "forbade");
      presentToSimplePastVerbMAP.put("forget", "forgot");
      presentToSimplePastVerbMAP.put("forgive", "forgave");
      presentToSimplePastVerbMAP.put("forsake", "forsook");
      presentToSimplePastVerbMAP.put("forswear", "forswore");
      presentToSimplePastVerbMAP.put("foretell", "foretold");
      presentToSimplePastVerbMAP.put("freeze", "froze");
      presentToSimplePastVerbMAP.put("get", "got");
      presentToSimplePastVerbMAP.put("give", "gave");
      presentToSimplePastVerbMAP.put("go", "went");
      presentToSimplePastVerbMAP.put("grind", "ground");
      presentToSimplePastVerbMAP.put("grow", "grew");
      presentToSimplePastVerbMAP.put("hang", "hung");
      presentToSimplePastVerbMAP.put("have", "had");
      presentToSimplePastVerbMAP.put("hang", "hanged");
      presentToSimplePastVerbMAP.put("hear", "heard");
      presentToSimplePastVerbMAP.put("hide", "hid");
      presentToSimplePastVerbMAP.put("hold", "held");
      presentToSimplePastVerbMAP.put("hurt", "hurt");
      presentToSimplePastVerbMAP.put("keep", "kept");
      presentToSimplePastVerbMAP.put("kneel", "knelt");
      presentToSimplePastVerbMAP.put("knit", "knit");
      presentToSimplePastVerbMAP.put("know", "knew");
      presentToSimplePastVerbMAP.put("lay", "laid");
      presentToSimplePastVerbMAP.put("lead", "led");
      presentToSimplePastVerbMAP.put("leap", "leapt");
      presentToSimplePastVerbMAP.put("learn", "learnt");
      presentToSimplePastVerbMAP.put("leave", "left");
      presentToSimplePastVerbMAP.put("lend", "lent");
      presentToSimplePastVerbMAP.put("let", "let");
      presentToSimplePastVerbMAP.put("lie", "lay");
      presentToSimplePastVerbMAP.put("light", "lighted");
      presentToSimplePastVerbMAP.put("lose", "lost");
      presentToSimplePastVerbMAP.put("make", "made");
      presentToSimplePastVerbMAP.put("mean", "meant");
      presentToSimplePastVerbMAP.put("meet", "met");
      presentToSimplePastVerbMAP.put("mislay", "mislaid");
      presentToSimplePastVerbMAP.put("mislead", "misled");
      presentToSimplePastVerbMAP.put("misspeak", "misspoke");
      presentToSimplePastVerbMAP.put("misspend", "misspent");
      presentToSimplePastVerbMAP.put("pay", "paid");
      presentToSimplePastVerbMAP.put("prove", "proved");
      presentToSimplePastVerbMAP.put("put", "put");
      presentToSimplePastVerbMAP.put("quit", "quit");
      presentToSimplePastVerbMAP.put("read", "read");
      presentToSimplePastVerbMAP.put("ride", "rode");
      presentToSimplePastVerbMAP.put("ring", "rang");
      presentToSimplePastVerbMAP.put("rise", "rose");
      presentToSimplePastVerbMAP.put("run", "ran");
      presentToSimplePastVerbMAP.put("saw", "sawed");
      presentToSimplePastVerbMAP.put("say", "said");
      presentToSimplePastVerbMAP.put("see", "saw");
      presentToSimplePastVerbMAP.put("seek", "sought");
      presentToSimplePastVerbMAP.put("sell", "sold");
      presentToSimplePastVerbMAP.put("send", "sent");
      presentToSimplePastVerbMAP.put("set", "set");
      presentToSimplePastVerbMAP.put("shake", "shook");
      presentToSimplePastVerbMAP.put("shed", "shed");
      presentToSimplePastVerbMAP.put("shine", "shone");
      presentToSimplePastVerbMAP.put("shoe", "shod");
      presentToSimplePastVerbMAP.put("shoot", "shot");
      presentToSimplePastVerbMAP.put("show", "showed");
      presentToSimplePastVerbMAP.put("shrink", "shrank");
      presentToSimplePastVerbMAP.put("shut", "shut");
      presentToSimplePastVerbMAP.put("sing", "sang");
      presentToSimplePastVerbMAP.put("sink", "sank");
      presentToSimplePastVerbMAP.put("sit", "sat");
      presentToSimplePastVerbMAP.put("slay", "slew");
      presentToSimplePastVerbMAP.put("sleep", "slept");
      presentToSimplePastVerbMAP.put("slide", "slid");
      presentToSimplePastVerbMAP.put("sling", "slung");
      presentToSimplePastVerbMAP.put("speak", "spoke");
      presentToSimplePastVerbMAP.put("spend", "spent");
      presentToSimplePastVerbMAP.put("spin", "spun");
      presentToSimplePastVerbMAP.put("spread", "spread");
      presentToSimplePastVerbMAP.put("spring", "sprang");
      presentToSimplePastVerbMAP.put("stand", "stood");
      presentToSimplePastVerbMAP.put("steal", "stole");
      presentToSimplePastVerbMAP.put("stick", "stuck");
      presentToSimplePastVerbMAP.put("sting", "stung");
      presentToSimplePastVerbMAP.put("stink", "stank");
      presentToSimplePastVerbMAP.put("strike", "struck");
      presentToSimplePastVerbMAP.put("string", "strung");
      presentToSimplePastVerbMAP.put("strive", "strove");
      presentToSimplePastVerbMAP.put("swear", "swore");
      presentToSimplePastVerbMAP.put("sweep", "swept");
      presentToSimplePastVerbMAP.put("swim", "swam");
      presentToSimplePastVerbMAP.put("swing", "swung");
      presentToSimplePastVerbMAP.put("take", "took");
      presentToSimplePastVerbMAP.put("teach", "taught");
      presentToSimplePastVerbMAP.put("tear", "tore");
      presentToSimplePastVerbMAP.put("tell", "told");
      presentToSimplePastVerbMAP.put("think", "thought");
      presentToSimplePastVerbMAP.put("throw", "threw");
      presentToSimplePastVerbMAP.put("tread", "trod");
      presentToSimplePastVerbMAP.put("wake", "woke");
      presentToSimplePastVerbMAP.put("wear", "wore");
      presentToSimplePastVerbMAP.put("understand", "understood");
      presentToSimplePastVerbMAP.put("win", "won");
      presentToSimplePastVerbMAP.put("wind", "wound");
      presentToSimplePastVerbMAP.put("wring", "wrung");
      presentToSimplePastVerbMAP.put("write", "wrote");
      presentToSimplePastVerbMAP.put("is", "was");
      presentToSimplePastVerbMAP.put("are", "was");

      simplePastToPresentVerbMAP = new HashMap<String, String>();

      simplePastToPresentVerbMAP.put("arose", "arise");
      simplePastToPresentVerbMAP.put("awoke", "awake");
      simplePastToPresentVerbMAP.put("was", "am");
      simplePastToPresentVerbMAP.put("bore", "bear");
      simplePastToPresentVerbMAP.put("beat", "beat");
      simplePastToPresentVerbMAP.put("became", "become");
      simplePastToPresentVerbMAP.put("began", "begin");
      simplePastToPresentVerbMAP.put("bent", "bend");
      simplePastToPresentVerbMAP.put("bet", "bet");
      simplePastToPresentVerbMAP.put("bound", "bind");
      simplePastToPresentVerbMAP.put("bit", "bite");
      simplePastToPresentVerbMAP.put("bled", "bleed");
      simplePastToPresentVerbMAP.put("blew", "blow");
      simplePastToPresentVerbMAP.put("bred", "breed");
      simplePastToPresentVerbMAP.put("brought", "bring");
      simplePastToPresentVerbMAP.put("built", "build");
      simplePastToPresentVerbMAP.put("burned", "burn");
      simplePastToPresentVerbMAP.put("bought", "buy");
      simplePastToPresentVerbMAP.put("cast", "cast");
      simplePastToPresentVerbMAP.put("caught", "catch");
      simplePastToPresentVerbMAP.put("chose", "choose");
      simplePastToPresentVerbMAP.put("clung", "cling");
      simplePastToPresentVerbMAP.put("came", "come");
      simplePastToPresentVerbMAP.put("cost", "cost");
      simplePastToPresentVerbMAP.put("crept", "creep");
      simplePastToPresentVerbMAP.put("cut", "cut");
      simplePastToPresentVerbMAP.put("dealt", "deal");
      simplePastToPresentVerbMAP.put("dug", "dig");
      simplePastToPresentVerbMAP.put("dived", "dive");
      simplePastToPresentVerbMAP.put("did", "do");
      simplePastToPresentVerbMAP.put("dragged", "drag");
      simplePastToPresentVerbMAP.put("drew", "draw");
      simplePastToPresentVerbMAP.put("dreamed", "dream");
      simplePastToPresentVerbMAP.put("drank", "drink");
      simplePastToPresentVerbMAP.put("drove", "drive");
      simplePastToPresentVerbMAP.put("drowned", "drown");
      simplePastToPresentVerbMAP.put("ate", "eat");
      simplePastToPresentVerbMAP.put("fell", "fall");
      simplePastToPresentVerbMAP.put("fed", "feed");
      simplePastToPresentVerbMAP.put("felt", "feel");
      simplePastToPresentVerbMAP.put("fought", "fight");
      simplePastToPresentVerbMAP.put("found", "find");
      simplePastToPresentVerbMAP.put("fit", "fit");
      simplePastToPresentVerbMAP.put("fled", "flee");
      simplePastToPresentVerbMAP.put("flung", "fling");
      simplePastToPresentVerbMAP.put("flew", "fly");
      simplePastToPresentVerbMAP.put("forbade", "forbid");
      simplePastToPresentVerbMAP.put("forgot", "forget");
      simplePastToPresentVerbMAP.put("forgave", "forgive");
      simplePastToPresentVerbMAP.put("forsook", "forsake");
      simplePastToPresentVerbMAP.put("forswore", "forswear");
      simplePastToPresentVerbMAP.put("foretold", "foretell");
      simplePastToPresentVerbMAP.put("froze", "freeze");
      simplePastToPresentVerbMAP.put("got", "get");
      simplePastToPresentVerbMAP.put("gave", "give");
      simplePastToPresentVerbMAP.put("went", "go");
      simplePastToPresentVerbMAP.put("ground", "grind");
      simplePastToPresentVerbMAP.put("grew", "grow");
      simplePastToPresentVerbMAP.put("hung", "hang");
      simplePastToPresentVerbMAP.put("had", "have");
      simplePastToPresentVerbMAP.put("hanged", "hang");
      simplePastToPresentVerbMAP.put("heard", "hear");
      simplePastToPresentVerbMAP.put("hid", "hide");
      simplePastToPresentVerbMAP.put("held", "hold");
      simplePastToPresentVerbMAP.put("hurt", "hurt");
      simplePastToPresentVerbMAP.put("kept", "keep");
      simplePastToPresentVerbMAP.put("knelt", "kneel");
      simplePastToPresentVerbMAP.put("knit", "knit");
      simplePastToPresentVerbMAP.put("knew", "know");
      simplePastToPresentVerbMAP.put("laid", "lay");
      simplePastToPresentVerbMAP.put("led", "lead");
      simplePastToPresentVerbMAP.put("leapt", "leap");
      simplePastToPresentVerbMAP.put("learnt", "learn");
      simplePastToPresentVerbMAP.put("left", "leave");
      simplePastToPresentVerbMAP.put("lent", "lend");
      simplePastToPresentVerbMAP.put("let", "let");
      simplePastToPresentVerbMAP.put("lay", "lie");
      simplePastToPresentVerbMAP.put("lighted", "light");
      simplePastToPresentVerbMAP.put("lost", "lose");
      simplePastToPresentVerbMAP.put("made", "make");
      simplePastToPresentVerbMAP.put("meant", "mean");
      simplePastToPresentVerbMAP.put("met", "meet");
      simplePastToPresentVerbMAP.put("mislaid", "mislay");
      simplePastToPresentVerbMAP.put("misled", "mislead");
      simplePastToPresentVerbMAP.put("misspoke", "misspeak");
      simplePastToPresentVerbMAP.put("misspent", "misspend");
      simplePastToPresentVerbMAP.put("paid", "pay");
      simplePastToPresentVerbMAP.put("proved", "prove");
      simplePastToPresentVerbMAP.put("put", "put");
      simplePastToPresentVerbMAP.put("quit", "quit");
      simplePastToPresentVerbMAP.put("read", "read");
      simplePastToPresentVerbMAP.put("rode", "ride");
      simplePastToPresentVerbMAP.put("rang", "ring");
      simplePastToPresentVerbMAP.put("rose", "rise");
      simplePastToPresentVerbMAP.put("ran", "run");
      simplePastToPresentVerbMAP.put("sawed", "saw");
      simplePastToPresentVerbMAP.put("said", "say");
      simplePastToPresentVerbMAP.put("saw", "see");
      simplePastToPresentVerbMAP.put("sought", "seek");
      simplePastToPresentVerbMAP.put("sold", "sell");
      simplePastToPresentVerbMAP.put("sent", "send");
      simplePastToPresentVerbMAP.put("set", "set");
      simplePastToPresentVerbMAP.put("shook", "shake");
      simplePastToPresentVerbMAP.put("shed", "shed");
      simplePastToPresentVerbMAP.put("shone", "shine");
      simplePastToPresentVerbMAP.put("shod", "shoe");
      simplePastToPresentVerbMAP.put("shot", "shoot");
      simplePastToPresentVerbMAP.put("showed", "show");
      simplePastToPresentVerbMAP.put("shrank", "shrink");
      simplePastToPresentVerbMAP.put("shut", "shut");
      simplePastToPresentVerbMAP.put("sang", "sing");
      simplePastToPresentVerbMAP.put("sank", "sink");
      simplePastToPresentVerbMAP.put("sat", "sit");
      simplePastToPresentVerbMAP.put("slew", "slay");
      simplePastToPresentVerbMAP.put("slept", "sleep");
      simplePastToPresentVerbMAP.put("slid", "slide");
      simplePastToPresentVerbMAP.put("slung", "sling");
      simplePastToPresentVerbMAP.put("spoke", "speak");
      simplePastToPresentVerbMAP.put("spent", "spend");
      simplePastToPresentVerbMAP.put("spun", "spin");
      simplePastToPresentVerbMAP.put("spread", "spread");
      simplePastToPresentVerbMAP.put("sprang", "spring");
      simplePastToPresentVerbMAP.put("stood", "stand");
      simplePastToPresentVerbMAP.put("stole", "steal");
      simplePastToPresentVerbMAP.put("stuck", "stick");
      simplePastToPresentVerbMAP.put("stung", "sting");
      simplePastToPresentVerbMAP.put("stank", "stink");
      simplePastToPresentVerbMAP.put("struck", "strike");
      simplePastToPresentVerbMAP.put("strung", "string");
      simplePastToPresentVerbMAP.put("strove", "strive");
      simplePastToPresentVerbMAP.put("swore", "swear");
      simplePastToPresentVerbMAP.put("swept", "sweep");
      simplePastToPresentVerbMAP.put("swam", "swim");
      simplePastToPresentVerbMAP.put("swung", "swing");
      simplePastToPresentVerbMAP.put("took", "take");
      simplePastToPresentVerbMAP.put("taught", "teach");
      simplePastToPresentVerbMAP.put("tore", "tear");
      simplePastToPresentVerbMAP.put("told", "tell");
      simplePastToPresentVerbMAP.put("thought", "think");
      simplePastToPresentVerbMAP.put("threw", "throw");
      simplePastToPresentVerbMAP.put("trod", "tread");
      simplePastToPresentVerbMAP.put("woke", "wake");
      simplePastToPresentVerbMAP.put("wore", "wear");
      simplePastToPresentVerbMAP.put("understood", "understand");
      simplePastToPresentVerbMAP.put("won", "win");
      simplePastToPresentVerbMAP.put("wound", "wind");
      simplePastToPresentVerbMAP.put("wrung", "wring");
      simplePastToPresentVerbMAP.put("wrote", "write");
      simplePastToPresentVerbMAP.put("was", "is");
      simplePastToPresentVerbMAP.put("was", "are");

      // load prsentToPastParticipleVerbMAP
      presentToPastParticipleVerbMAP = new HashMap<String, String>();
      presentToPastParticipleVerbMAP.put("arise", "arisen");
      presentToPastParticipleVerbMAP.put("awake", "awoken");
      presentToPastParticipleVerbMAP.put("am", "were");
      presentToPastParticipleVerbMAP.put("bear", "borne");
      presentToPastParticipleVerbMAP.put("beat", "beaten");
      presentToPastParticipleVerbMAP.put("become", "become");
      presentToPastParticipleVerbMAP.put("begin", "begun");
      presentToPastParticipleVerbMAP.put("bend", "bent");
      presentToPastParticipleVerbMAP.put("bet", "bet");
      presentToPastParticipleVerbMAP.put("bind", "bound");
      presentToPastParticipleVerbMAP.put("bite", "bitten");
      presentToPastParticipleVerbMAP.put("bleed", "bled");
      presentToPastParticipleVerbMAP.put("blow", "blown");
      presentToPastParticipleVerbMAP.put("breed", "bred");
      presentToPastParticipleVerbMAP.put("bring", "brought");
      presentToPastParticipleVerbMAP.put("build", "built");
      presentToPastParticipleVerbMAP.put("burn", "burnt");
      presentToPastParticipleVerbMAP.put("buy", "bought");
      presentToPastParticipleVerbMAP.put("cast", "cast");
      presentToPastParticipleVerbMAP.put("catch", "caught");
      presentToPastParticipleVerbMAP.put("choose", "chosen");
      presentToPastParticipleVerbMAP.put("cling", "clung");
      presentToPastParticipleVerbMAP.put("come", "come");
      presentToPastParticipleVerbMAP.put("cost", "cost");
      presentToPastParticipleVerbMAP.put("creep", "crept");
      presentToPastParticipleVerbMAP.put("cut", "cut");
      presentToPastParticipleVerbMAP.put("deal", "dealt");
      presentToPastParticipleVerbMAP.put("dig", "dug");
      presentToPastParticipleVerbMAP.put("dive", "dove");
      presentToPastParticipleVerbMAP.put("do", "done");
      presentToPastParticipleVerbMAP.put("drag", "dragged");
      presentToPastParticipleVerbMAP.put("draw", "drawn");
      presentToPastParticipleVerbMAP.put("dream", "dreamt");
      presentToPastParticipleVerbMAP.put("drink", "drunk");
      presentToPastParticipleVerbMAP.put("drive", "driven");
      presentToPastParticipleVerbMAP.put("drown", "drowned");
      presentToPastParticipleVerbMAP.put("eat", "eaten");
      presentToPastParticipleVerbMAP.put("fall", "fallen");
      presentToPastParticipleVerbMAP.put("feed", "fed");
      presentToPastParticipleVerbMAP.put("feel", "felt");
      presentToPastParticipleVerbMAP.put("fight", "fought");
      presentToPastParticipleVerbMAP.put("find", "found");
      presentToPastParticipleVerbMAP.put("fit", "fit");
      presentToPastParticipleVerbMAP.put("flee", "fled");
      presentToPastParticipleVerbMAP.put("fling", "flung");
      presentToPastParticipleVerbMAP.put("fly", "flown");
      presentToPastParticipleVerbMAP.put("forbid", "forbidden");
      presentToPastParticipleVerbMAP.put("forget", "forgotten");
      presentToPastParticipleVerbMAP.put("forgive", "forgiven");
      presentToPastParticipleVerbMAP.put("forsake", "forsaken");
      presentToPastParticipleVerbMAP.put("forswear", "forsworn");
      presentToPastParticipleVerbMAP.put("foretell", "foretold");
      presentToPastParticipleVerbMAP.put("freeze", "frozen");
      presentToPastParticipleVerbMAP.put("get", "gotten");
      presentToPastParticipleVerbMAP.put("give", "given");
      presentToPastParticipleVerbMAP.put("go", "gone");
      presentToPastParticipleVerbMAP.put("grind", "ground");
      presentToPastParticipleVerbMAP.put("grow", "grown");
      presentToPastParticipleVerbMAP.put("hang", "hung");
      presentToPastParticipleVerbMAP.put("have", "had");
      presentToPastParticipleVerbMAP.put("hang", "hanged");
      presentToPastParticipleVerbMAP.put("hear", "heard");
      presentToPastParticipleVerbMAP.put("hide", "hidden");
      presentToPastParticipleVerbMAP.put("hold", "held");
      presentToPastParticipleVerbMAP.put("hurt", "hurt");
      presentToPastParticipleVerbMAP.put("keep", "kept");
      presentToPastParticipleVerbMAP.put("kneel", "kneeled");
      presentToPastParticipleVerbMAP.put("knit", "knitted");
      presentToPastParticipleVerbMAP.put("know", "known");
      presentToPastParticipleVerbMAP.put("lay", "laid");
      presentToPastParticipleVerbMAP.put("lead", "led");
      presentToPastParticipleVerbMAP.put("leap", "leaped");
      presentToPastParticipleVerbMAP.put("learn", "learned");
      presentToPastParticipleVerbMAP.put("leave", "left");
      presentToPastParticipleVerbMAP.put("lend", "lent");
      presentToPastParticipleVerbMAP.put("let", "let");
      presentToPastParticipleVerbMAP.put("lie", "lain");
      presentToPastParticipleVerbMAP.put("light", "lit");
      presentToPastParticipleVerbMAP.put("lose", "lost");
      presentToPastParticipleVerbMAP.put("make", "made");
      presentToPastParticipleVerbMAP.put("mean", "meant");
      presentToPastParticipleVerbMAP.put("meet", "met");
      presentToPastParticipleVerbMAP.put("mislay", "mislaid");
      presentToPastParticipleVerbMAP.put("mislead", "misled");
      presentToPastParticipleVerbMAP.put("misspeak", "misspoken");
      presentToPastParticipleVerbMAP.put("misspend", "misspent");
      presentToPastParticipleVerbMAP.put("pay", "paid");
      presentToPastParticipleVerbMAP.put("prove", "proved");
      presentToPastParticipleVerbMAP.put("put", "put");
      presentToPastParticipleVerbMAP.put("quit", "quit");
      presentToPastParticipleVerbMAP.put("read", "read");
      presentToPastParticipleVerbMAP.put("ride", "ridden");
      presentToPastParticipleVerbMAP.put("ring", "rung");
      presentToPastParticipleVerbMAP.put("rise", "risen");
      presentToPastParticipleVerbMAP.put("run", "run");
      presentToPastParticipleVerbMAP.put("saw", "sawn");
      presentToPastParticipleVerbMAP.put("say", "said");
      presentToPastParticipleVerbMAP.put("see", "seen");
      presentToPastParticipleVerbMAP.put("seek", "sought");
      presentToPastParticipleVerbMAP.put("sell", "sold");
      presentToPastParticipleVerbMAP.put("send", "sent");
      presentToPastParticipleVerbMAP.put("set", "set");
      presentToPastParticipleVerbMAP.put("shake", "shaken");
      presentToPastParticipleVerbMAP.put("shed", "shed");
      presentToPastParticipleVerbMAP.put("shine", "shone");
      presentToPastParticipleVerbMAP.put("shoe", "shod");
      presentToPastParticipleVerbMAP.put("shoot", "shot");
      presentToPastParticipleVerbMAP.put("show", "shown");
      presentToPastParticipleVerbMAP.put("shrink", "shrunk");
      presentToPastParticipleVerbMAP.put("shut", "shut");
      presentToPastParticipleVerbMAP.put("sing", "sung");
      presentToPastParticipleVerbMAP.put("sink", "sunk");
      presentToPastParticipleVerbMAP.put("sit", "sat");
      presentToPastParticipleVerbMAP.put("slay", "slain");
      presentToPastParticipleVerbMAP.put("sleep", "slept");
      presentToPastParticipleVerbMAP.put("slide", "slid");
      presentToPastParticipleVerbMAP.put("sling", "slung");
      presentToPastParticipleVerbMAP.put("speak", "spoken");
      presentToPastParticipleVerbMAP.put("spend", "spent");
      presentToPastParticipleVerbMAP.put("spin", "spun");
      presentToPastParticipleVerbMAP.put("spread", "spread");
      presentToPastParticipleVerbMAP.put("spring", "sprung");
      presentToPastParticipleVerbMAP.put("stand", "stood");
      presentToPastParticipleVerbMAP.put("steal", "stolen");
      presentToPastParticipleVerbMAP.put("stick", "stuck");
      presentToPastParticipleVerbMAP.put("sting", "stung");
      presentToPastParticipleVerbMAP.put("stink", "stunk");
      presentToPastParticipleVerbMAP.put("strike", "struck");
      presentToPastParticipleVerbMAP.put("string", "strung");
      presentToPastParticipleVerbMAP.put("strive", "striven");
      presentToPastParticipleVerbMAP.put("swear", "sworn");
      presentToPastParticipleVerbMAP.put("sweep", "swept");
      presentToPastParticipleVerbMAP.put("swim", "swum");
      presentToPastParticipleVerbMAP.put("swing", "swung");
      presentToPastParticipleVerbMAP.put("take", "taken");
      presentToPastParticipleVerbMAP.put("teach", "taught");
      presentToPastParticipleVerbMAP.put("tear", "torn");
      presentToPastParticipleVerbMAP.put("tell", "told");
      presentToPastParticipleVerbMAP.put("think", "thought");
      presentToPastParticipleVerbMAP.put("throw", "thrown");
      presentToPastParticipleVerbMAP.put("tread", "trodden");
      presentToPastParticipleVerbMAP.put("wake", "woken");
      presentToPastParticipleVerbMAP.put("wear", "worn");
      presentToPastParticipleVerbMAP.put("understand", "understood");
      presentToPastParticipleVerbMAP.put("win", "won");
      presentToPastParticipleVerbMAP.put("wind", "wound");
      presentToPastParticipleVerbMAP.put("wring", "wrung");
      presentToPastParticipleVerbMAP.put("write", "written");
      presentToPastParticipleVerbMAP.put("is", "were");
      presentToPastParticipleVerbMAP.put("are", "were");

   }

   public String getSimplePast (String verb) {
      if (verb.endsWith("ed")) {
         return verb;
      }
      // check if its in the irregular Verb
      if (presentToSimplePastVerbMAP.containsKey(verb)) {
         return presentToSimplePastVerbMAP.get(verb);
      } else {
         return getRegularVerbVariation(verb);
      }
   }

   public String getPastParticiple (String verb) {
      if (verb.endsWith("ed")) {
         return verb;
      }
      // check if its in the irregular Verb
      if (presentToPastParticipleVerbMAP.containsKey(verb)) {
         return presentToPastParticipleVerbMAP.get(verb);
      } else {
         return getRegularVerbVariation(verb);
      }
   }

   public String getPresentParticiple (String verb) {
      if (verb.endsWith("ing")) {
         return verb;
      }
      if (verb.endsWith("ed")) {
         verb = verb.substring(0, verb.length() - 2);
         verb = verb + "ing";
         return verb;
      }
      if (verb.endsWith("e")) {
         verb = verb.substring(0, verb.length());
         verb = verb + "ing";
         return verb;
      }
      if (verb.endsWith("ie")) {
         verb = verb.substring(0, verb.length() - 2);
         verb = verb + "y";
         return verb + "ing";
      }
      if (verb.endsWith("c")) {
         verb = verb + "k";
         return verb + "ing";
      }

      return verb + "ing";
   }

   public String getSimpleVerbFromPresentParticiple (String verbForm) {
      if (!verbForm.endsWith("ing")) {
         return verbForm;
      } else {
         verbForm = verbForm.substring(0, verbForm.length() - 3);
         return verbForm;
      }
   }

   public String getVerbFromSimplePast (String verbForm) {
      // check if its in the irregular Verb
      if (simplePastToPresentVerbMAP.containsKey(verbForm)) {
         return simplePastToPresentVerbMAP.get(verbForm);
      } else {
         if (verbForm.endsWith("ed")) {
            return verbForm.substring(0, verbForm.length() - 2);
         } else if (verbForm.endsWith("d")) {
            return verbForm.substring(0, verbForm.length() - 1);
         }
      }
      return verbForm;
   }

   public String getVerbFromPastParticiple (String verbForm) {
      if (verbForm.endsWith("ed")) {
         return verbForm.substring(0, verbForm.length() - 2);
      } else if (verbForm.endsWith("d")) {
         return verbForm.substring(0, verbForm.length() - 1);
      }
      return verbForm;
   }

   /**
    * @param verb
    * @return
    */
   private String getRegularVerbVariation (String verb) {
      // if its regular Verb and ends with "e" append "d" at the end
      if (verb.endsWith("e")) {
         return verb + "e";
      }
      // for all other cases just append "ed"
      return verb + "ed";
   }

}
